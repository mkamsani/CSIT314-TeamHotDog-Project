#!/bin/sh

sudo -u postgres psql -f ~/ctbs/schema.sql
sudo -u postgres psql -f ~/ctbs/trigger.sql
sudo -u postgres psql -f ~/ctbs/data_base.sql
sudo -u postgres psql -f ~/ctbs/data_many.sql