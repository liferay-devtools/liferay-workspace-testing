#!/usr/bin/env bash

for i in devcon*.markdown; do
    echo "Processing markmap-cli $i"
    npx markmap-cli --no-open $i
done