create or replace procedure httpScada()
    language plpgsql
as
$$
declare
    url                  varchar;
    declare pathAccess   varchar;
    declare clientId     varchar;
    declare clientSecret varchar;
    declare cs           varchar;
    declare grantType    varchar;
    declare reqJson      varchar;
    declare statusCode   int4;
    declare authJson     json;
    declare accessToken  varchar;
    declare authRow      RECORD;
    declare pathFact     varchar;
    declare factJson     json;
    DECLARE factCnt      int;
    declare fact         JSON;
    DECLARE rcnt         int;
begin
    url := 'http://10.200.6.72:8082/';
    pathAccess := 'hdl/oauth/v1.0/access.json';
    clientId := 'lousun';
    clientSecret := '25396879';
    grantType := 'client_credentials';
    pathFact := 'hdl/scada/v1.0/factory/monitors.json';

    accessToken := '';

    -- find most recent auth
    select auth.accessToken, auth.respTime, auth.expiresIn
    into authRow
    from scada.scada_auth_list AS auth
    order by auth.respTime desc
    limit 1;
    if found then
        if authRow.respTime + interval '1 second' * authRow.expiresIn > now() + interval '5 minute' then
            accessToken := authRow.accessToken;
            raise notice 'using exist accessToken: %', accessToken;
        end if;
    end if;

    -- accessToken not exist in database
    perform http_set_curlopt('CURLOPT_TIMEOUT', '10');
    if length(accessToken) < 1 then
        cs := substring(digest(clientId || '$' || clientSecret, 'sha256')::text from 3);
        reqJson := json_object(
                array ['grant_type', grantType, 'client_id', clientId, 'client_secret', cs]::text[]);
        raise notice 'access.json request: %', reqJson;

        select http.status, http.content::json
        into statusCode, authJson
        from http_post(url || pathAccess, reqJson, 'application/json') AS http;
        raise notice 'access.json got %: %', statusCode, authJson;
        accessToken := authJson::json ->> 'access_token';

        insert into scada.scada_auth_list(reqTime, grantType, clientId, clientSecret,
                                          respTime, statusCode, retFine,
                                          tokenType, accessToken, refreshToken, expiresIn,
                                          respText)
        values (now(), grantType, clientId, clientSecret,
                now(), statusCode, length(accessToken) > 0,
                authJson ->> 'token_type', accessToken, authJson ->> 'refresh_token', (authJson ->> 'expires_in')::int,
                authJson::TEXT);

        commit;
    end if;

    reqJson := json_object(ARRAY['access_token', accessToken]::text[])::text;
    raise notice 'monitors.json request: %', reqJson;
    select http.status, http.content::json
    into statusCode, factJson
    from http_post(url || pathFact, reqJson, 'application/json') AS http;

    raise notice 'monitors.json response %: %', statusCode, factJson;

    -- insert fact
    statusCode := (factJson ->> 'Code')::int;
    if statusCode != 0 then
        raise notice 'fail to list fact: %', statusCode;
    end if;

    select json_array_length(factJson->'Response') into factCnt;
    for i in 0..(factCnt - 1)
        loop
            fact := factJson->'Response'->i;
            raise notice 'proceed factory group:%, code:%, %', fact->>'group', fact->>'code', fact::text;
            update scada.scada_stat
            set fname    = fact ->> 'name',
                sname    = fact ->> 'sname',
                mty      = fact ->> 'mty',
                position = fact ->> 'position',
                sensors  = fact -> 'sensors'
            where fgroup = fact ->> 'group'
              and fcode = fact ->> 'code';
            get DIAGNOSTICS rcnt := ROW_COUNT;

            if rcnt < 1 then
                insert into scada.scada_stat(fgroup, fcode, fname, sname, mty, position, sensors)
                values (fact ->> 'group',
                        fact ->> 'code',
                        fact ->> 'name',
                        fact ->> 'sname',
                        fact ->> 'mty',
                        fact ->> 'position',
                        fact -> 'sensors');
            end if;
        end loop;
end;

$$;

