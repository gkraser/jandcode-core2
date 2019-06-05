#! /bin/sh

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
UPD_CM_DEPEND="${UPD_CM_DEPEND} ${ROOTDIR}/jandcode-commons/src ${ROOTDIR}/jandcode-commons/project.jc"
UPD_CM_DEPEND="${UPD_CM_DEPEND} ${ROOTDIR}/jandcode-commons-jansi/src ${ROOTDIR}/jandcode-commons-jansi/project.jc"
UPD_CM_DEPEND="${UPD_CM_DEPEND} ${ROOTDIR}/jandcode-commons-groovy/src ${ROOTDIR}/jandcode-commons-groovy/project.jc"
UPD_CM_DEPEND="${UPD_CM_DEPEND} ${ROOTDIR}/jandcode-commons-moduledef/src ${ROOTDIR}/jandcode-commons-moduledef/project.jc"
UPD_CM_DEPEND="${UPD_CM_DEPEND} ${ROOTDIR}/jandcode-jc/src ${ROOTDIR}/jandcode-jc/project.jc"

UPD_CM="ls -l -R --full-time -n ${UPD_CM_DEPEND}"

${UPD_CM} > ${UPD2}
if ! cmp -s "${UPD1}" "${UPD2}" ; then
   BLD1=yes
fi

if [ "${BLD1}" = "yes" ]; then
  gradle copyLibs || exit 1
  sh ./ant.sh build.all || exit 1
  ${UPD_CM} > ${UPD1}
fi

cd ${T1}

