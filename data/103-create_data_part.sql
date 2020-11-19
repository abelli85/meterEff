-- postgresql partitions for bw_data
create table bw_data_2017 (like bw_data including all);
create table bw_data_2018 (like bw_data including all);
create table bw_data_2019 (like bw_data including all);
create table bw_data_2020 (like bw_data including all);
create table bw_data_2021 (like bw_data including all);
create table bw_data_2022 (like bw_data including all);
create table bw_data_2023 (like bw_data including all);
create table bw_data_2024 (like bw_data including all);
create table bw_data_2025 (like bw_data including all);
create table bw_data_2026 (like bw_data including all);

alter table bw_data_2017 add constraint bw_data_2017_check check ( sampletime < '2018-1-1'::date );
alter table bw_data_2018 add constraint bw_data_2018_check check ( sampletime >= '2018-1-1'::date and sampletime < '2019-1-1'::date );
alter table bw_data_2019 add constraint bw_data_2019_check check ( sampletime >= '2019-1-1'::date and sampletime < '2020-1-1'::date );
alter table bw_data_2020 add constraint bw_data_2020_check check ( sampletime >= '2020-1-1'::date and sampletime < '2021-1-1'::date );
alter table bw_data_2021 add constraint bw_data_2021_check check ( sampletime >= '2021-1-1'::date and sampletime < '2022-1-1'::date );
alter table bw_data_2022 add constraint bw_data_2022_check check ( sampletime >= '2022-1-1'::date and sampletime < '2023-1-1'::date );
alter table bw_data_2023 add constraint bw_data_2023_check check ( sampletime >= '2023-1-1'::date and sampletime < '2024-1-1'::date );
alter table bw_data_2024 add constraint bw_data_2024_check check ( sampletime >= '2024-1-1'::date and sampletime < '2025-1-1'::date );
alter table bw_data_2025 add constraint bw_data_2025_check check ( sampletime >= '2025-1-1'::date and sampletime < '2026-1-1'::date );
alter table bw_data_2026 add constraint bw_data_2026_check check ( sampletime >= '2026-1-1'::date );

create or replace function bw_data_part_fun()
returns trigger as $$
    begin
        if     ( sampletime < '2018-1-1'::date ) then insert into bw_data_2017 values (new.*);
        elseif ( sampletime >= '2018-1-1'::date and sampletime < '2019-1-1'::date ) then insert into bw_data_2018 values (new.*);
        elseif ( sampletime >= '2019-1-1'::date and sampletime < '2020-1-1'::date ) then insert into bw_data_2019 values (new.*);
        elseif ( sampletime >= '2020-1-1'::date and sampletime < '2021-1-1'::date ) then insert into bw_data_2020 values (new.*);
        elseif ( sampletime >= '2021-1-1'::date and sampletime < '2022-1-1'::date ) then insert into bw_data_2021 values (new.*);
        elseif ( sampletime >= '2022-1-1'::date and sampletime < '2023-1-1'::date ) then insert into bw_data_2022 values (new.*);
        elseif ( sampletime >= '2023-1-1'::date and sampletime < '2024-1-1'::date ) then insert into bw_data_2023 values (new.*);
        elseif ( sampletime >= '2024-1-1'::date and sampletime < '2025-1-1'::date ) then insert into bw_data_2024 values (new.*);
        elseif ( sampletime >= '2025-1-1'::date and sampletime < '2026-1-1'::date ) then insert into bw_data_2025 values (new.*);
        elseif ( sampletime >= '2026-1-1'::date ) then insert into bw_data_2026 values (new.*);
        end if;
    end;
    $$
language plpgsql;

create trigger bw_data_insert_trigger
    before insert on bw_data
    for each row execute procedure bw_data_part_fun();

/*
alter table bw_data_2017 no inherit bw_data;
alter table bw_data_2018 no inherit bw_data;
alter table bw_data_2019 no inherit bw_data;
alter table bw_data_2020 no inherit bw_data;
alter table bw_data_2021 no inherit bw_data;
alter table bw_data_2022 no inherit bw_data;
alter table bw_data_2023 no inherit bw_data;
alter table bw_data_2024 no inherit bw_data;
alter table bw_data_2025 no inherit bw_data;
alter table bw_data_2026 no inherit bw_data;
*/

-- backup all data.
create table bw_data_bak as select * from bw_data;

insert into bw_data_2017 select * from bw_data where sampletime < '2018-1-1'::date;
insert into bw_data_2018 select * from bw_data where sampletime >= '2018-1-1'::date and sampletime < '2019-1-1'::date;
insert into bw_data_2019 select * from bw_data where sampletime >= '2019-1-1'::date and sampletime < '2020-1-1'::date;
insert into bw_data_2020 select * from bw_data where sampletime >= '2020-1-1'::date and sampletime < '2021-1-1'::date;
insert into bw_data_2021 select * from bw_data where sampletime >= '2021-1-1'::date and sampletime < '2022-1-1'::date;
insert into bw_data_2022 select * from bw_data where sampletime >= '2022-1-1'::date and sampletime < '2023-1-1'::date;
insert into bw_data_2023 select * from bw_data where sampletime >= '2023-1-1'::date and sampletime < '2024-1-1'::date;
insert into bw_data_2024 select * from bw_data where sampletime >= '2024-1-1'::date and sampletime < '2025-1-1'::date;
insert into bw_data_2025 select * from bw_data where sampletime >= '2025-1-1'::date and sampletime < '2026-1-1'::date;
insert into bw_data_2026 select * from bw_data where sampletime >= '2026-1-1'::date;

delete from bw_data where sampletime < '2018-1-1'::date;
delete from bw_data where sampletime >= '2018-1-1'::date and sampletime < '2019-1-1'::date;
delete from bw_data where sampletime >= '2019-1-1'::date and sampletime < '2020-1-1'::date;
delete from bw_data where sampletime >= '2020-1-1'::date and sampletime < '2021-1-1'::date;
delete from bw_data where sampletime >= '2021-1-1'::date and sampletime < '2022-1-1'::date;
delete from bw_data where sampletime >= '2022-1-1'::date and sampletime < '2023-1-1'::date;
delete from bw_data where sampletime >= '2023-1-1'::date and sampletime < '2024-1-1'::date;
delete from bw_data where sampletime >= '2024-1-1'::date and sampletime < '2025-1-1'::date;
delete from bw_data where sampletime >= '2025-1-1'::date and sampletime < '2026-1-1'::date;
delete from bw_data where sampletime >= '2026-1-1'::date;

alter table bw_data_2017 inherit bw_data;
alter table bw_data_2018 inherit bw_data;
alter table bw_data_2019 inherit bw_data;
alter table bw_data_2020 inherit bw_data;
alter table bw_data_2021 inherit bw_data;
alter table bw_data_2022 inherit bw_data;
alter table bw_data_2023 inherit bw_data;
alter table bw_data_2024 inherit bw_data;
alter table bw_data_2025 inherit bw_data;
alter table bw_data_2026 inherit bw_data;
