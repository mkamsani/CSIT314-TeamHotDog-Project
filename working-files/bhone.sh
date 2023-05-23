#!/bin/sh

psql -U postgres -f ../database/schema.sql
psql -U postgres -f ../database/trigger.sql
psql -U postgres -f ../database/data_base.sql
psql -U postgres -f ../database/data_many.sql