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
