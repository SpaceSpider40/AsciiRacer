#!/bin/bash

./build.sh

cd "./dist" || exit

./racer.sh
