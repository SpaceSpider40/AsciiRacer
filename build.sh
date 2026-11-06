#!/bin/bash

set -e

mvn clean package

echo "Build finished"

chmod -R 755 dist/**/*.sh dist/*.sh

echo "Script permissions finished"