#!/bin/sh

psql -U postgres -f ./schema.sql
psql -U postgres -f ./trigger.sql
psql -U postgres -f ./data_base.sql
psql -U postgres -f ./data_many.sql