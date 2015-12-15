#!/bin/bash

mysqldump -u sqlalchemist --password=password sqlalchemist | gzip >  "/var/backups/sqlalchemist-$(date).sql"

