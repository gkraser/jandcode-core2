#! /bin/sh

if [ "${JC_PREPARE_RUN}" = "1" ]; then
    exit
fi
export JC_PREPARE_RUN=1

WD=`dirname $0`
WD=`realpath ${WD}`

T1=`pwd`
cd ${WD}

JCDIR=../../_jc
ROOTDIR=../..

if [ ! -e "${JCDIR}" ]; then
  mkdir -p "${JCDIR}"
fi

UPD1="${JCDIR}/upd1"
UPD2="${JCDIR}/upd2"

UPD_CM_DEPEND=
UPD_CM_DEPEND="${UPD_CM_DEPEND} ${ROOTDIR}/p-commons/jandcode-commons/src ${ROOTDIR}/p-commons/jandcode-commons/project.jc"
UPD_CM_DEPEND="${UPD_CM_DEPEND} ${ROOTDIR}/p-commons/jandcode-commons-jansi/src ${ROOTDIR}/p-commons/jandcode-commons-jansi/project.jc"
UPD_CM_DEPEND="${UPD_CM_DEPEND} ${ROOTDIR}/p-commons/jandcode-commons-groovy/src ${ROOTDIR}/p-commons/jandcode-commons-groovy/project.jc"
UPD_CM_DEPEND="${UPD_CM_DEPEND} ${ROOTDIR}/p-commons/jandcode-commons-moduledef/src ${ROOTDIR}/p-commons/jandcode-commons-moduledef/project.jc"
UPD_CM_DEPEND="${UPD_CM_DEPEND} ${ROOTDIR}/p-jc/jandcode-jc/src ${ROOTDIR}/p-jc/jandcode-jc/project.jc"
UPD_CM_DEPEND="${UPD_CM_DEPEND} ${ROOTDIR}/lib/lib.gradle"
UPD_CM_DEPEND="${UPD_CM_DEPEND} ${ROOTDIR}/jc-libs.txt"

UPD_CM="ls -l -R --full-time -n ${UPD_CM_DEPEND}"

${UPD_CM} > ${UPD2}
if ! cmp -s "${UPD1}" "${UPD2}" ; then
   BLD1=yes
fi

if [ "${BLD1}" = "yes" ]; then
  gradle copyLibs || exit 1
  sh ./ant.sh build.jc || exit 1
  sh ../../jc-run.sh -no-ansi showinfo || exit 1
  ${UPD_CM} > ${UPD1}
fi

cd ${T1}
