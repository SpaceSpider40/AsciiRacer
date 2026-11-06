#!/bin/bash

./build.sh

cd "./dist" || exit

kitty --hold ./racer.sh
