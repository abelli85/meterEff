create or replace function get_hzpy(vhz text) returns text[] as $$
declare
    res text[];
    tmp_py text;
    tmp_zm text;
begin
    for i in 1..length(vhz)
        loop
            select py,zm into tmp_py,tmp_zm from pinyin where hz=substring(vhz, i, 1);
            if not found then
                res := array_cat(res, array[substring(vhz, i, 1)]);
            else
                res := array_cat(res, array[tmp_py, tmp_zm, substring(vhz, i, 1)]);
            end if;
        end loop;
    return res;
end;
$$ language plpgsql strict immutable;

-- 字符串汉字 -> 拼音, 字母
create or replace function fetchPyzm(vhz text[]) returns text[] as $$
declare
    tmp_py text;
    tmp_zm text;
    rpy text;
    rzm text;
    res text[];
    tcnt int4;
    strcnt int4;
    ch text;
begin
    tcnt := array_length(vhz, 1);
    if tcnt is null then
        return res;
    end if;

    for i in 1..tcnt
    loop
        if vhz[i] is null then
            continue ;
        end if;
        strcnt := length(vhz[i]);
        if strcnt is null or strcnt < 1 then
            continue ;
        end if;

        rpy := '';
        rzm := '';
        for j in 1..strcnt
        loop
            ch := substr(vhz[i], j, 1);
            select py, zm into tmp_py, tmp_zm from pinyin where hz = ch;
            if FOUND then
                rpy := concat(rpy, tmp_py);
                rzm := concat(rzm, tmp_zm);
            end if;
        end loop;

        if length(rpy) > 0 then
            res := array_cat(res, array[lower(rpy), lower(rzm), lower(vhz[i])]);
            else
            res := array_cat(res, array[lower(vhz[i])]);
        end if;
    end loop;

    return res;
end;
$$ language plpgsql strict immutable;

select fetchPyzm(array ['北京', '深圳']);
select fetchPyzm(array ['北京', '深圳', 'world']);
select fetchPyzm(array ['Hello', 'world']);
select fetchPyzm( tsvector_to_array(to_tsvector('parser_name', '北京World深圳')));
select firmid, firmname from bw_firm
where fetchPyzm(tsvector_to_array(to_tsvector('parser_name', firmname)))
          @> tsvector_to_array(to_tsvector('parser_name', '南山Ns'));

create index idx_meter_name_gin on bw_meter using
	gin(fetchPyzm(tsvector_to_array(to_tsvector('parser_name', meterName))));

create table test(id int, info text);
create index idx on test using gin (get_hzpy(info));

-- test
insert into test values (1, '你好abx呵呵, ');
select * from test where get_hzpy(info) @> array['ni'];
select * from test where get_hzpy(info) @> array['NI'];
select * from test where get_hzpy(info) @> array['he'];

-- update
create index CONCURRENTLY idx_test_2 on test using gin(get_hzpy(info));
drop index idx;
