#!/bin/sh

if [ -f '/www/huangguan/hg3088/agents/_cli/flushWay/HuangGan.locks' ]; then
    php '/www/huangguan/hg3088/agents/app/agents/downdata_ra/bk/BK_RQ_tw.php'
fi