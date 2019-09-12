# base recipe: poky/meta/recipes-extended/psmisc/psmisc.inc
# base branch: warrior
# base commit: 47022546cfa6c509c8f11e52c3e6310ac68660a3

SUMMARY = "Utilities for managing processes on your system"
HOMEPAGE = "http://psmisc.sf.net/"
DESCRIPTION = "The psmisc package contains utilities for managing processes on your \
system: pstree, killall and fuser.  The pstree command displays a tree \
structure of all of the running processes on your system.  The killall \
command sends a specified signal (SIGTERM if nothing is specified) to \
processes identified by name.  The fuser command identifies the PIDs \
of processes that are using specified files or filesystems."
SECTION = "base"
DEPENDS = "ncurses virtual/libintl"
LICENSE = "GPLv2"

inherit debian-package
require recipes-debian/sources/psmisc.inc

DEBIAN_UNPACK_DIR = "${WORKDIR}/psmisc-${PV}"
S = "${WORKDIR}/psmisc-${PV}"

inherit autotools gettext

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'ipv6', d)}"
PACKAGECONFIG[ipv6] = "--enable-ipv6,--disable-ipv6,"

ALLOW_EMPTY_${PN} = "1"

PACKAGES =+ "fuser fuser-doc killall killall-doc pstree pstree-doc"
PACKAGES += "psmisc-extras"

FILES_${PN} = ""
RDEPENDS_${PN} = "fuser killall pstree"

FILES_fuser = "${bindir}/fuser.${BPN}"
FILES_fuser-doc = "${mandir}/man1/fuser*"

FILES_killall = "${bindir}/killall.${BPN}"
FILES_killall-doc = "${mandir}/man1/killall*"

FILES_pstree = "${bindir}/pstree"
FILES_pstree-doc = "${mandir}/man1/pstree*"

FILES_psmisc-extras = "${bindir}"
FILES_psmisc-extras-doc = "${mandir}"

inherit update-alternatives

ALTERNATIVE_PRIORITY = "90"

ALTERNATIVE_killall = "killall"

ALTERNATIVE_fuser = "fuser"