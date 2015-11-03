CREATE TABLESPACE tbs_perm
  DATAFILE 'perm.dat'
    SIZE 20M
  ONLINE;

CREATE TEMPORARY TABLESPACE tbs_temp
TEMPFILE 'temp.dbf'
SIZE 5M
AUTOEXTEND ON;

CREATE USER valentina
IDENTIFIED BY valentina
DEFAULT TABLESPACE tbs_perm
TEMPORARY TABLESPACE tbs_temp
QUOTA 20M on tbs_perm;

GRANT create session TO valentina;
GRANT create table TO valentina;
GRANT create view TO valentina;
GRANT create any trigger TO valentina;
GRANT create any procedure TO valentina;
GRANT create sequence TO valentina;
GRANT create synonym TO valentina;