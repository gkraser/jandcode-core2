
create function genid_nextid(g_name varchar(63), g_count bigint) returns bigint
begin
  update genid_data set val=last_insert_id(val+g_count*stepId) where name=g_name;
  return last_insert_id();
end

~~
