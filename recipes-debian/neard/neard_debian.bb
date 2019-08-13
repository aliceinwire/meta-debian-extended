# base recipe: ./meta/recipes-connectivity/neard/neard_0.16.bb
# base branch: warrior
# base commit: a1eb378771bcb427a60c56b8bfdbc93948917213

SUMMARY = "Linux NFC daemon"
DESCRIPTION = "A daemon for the Linux Near Field Communication stack"
HOMEPAGE = "http://01.org/linux-nfc"
LICENSE = "GPLv2"

DEPENDS = "dbus glib-2.0 libnl"

inherit debian-package
require recipes-debian/sources/${BPN}.inc
DEBIAN_QUILT_PATCHES = ""

FILESPATH_append = ":${COREBASE}/meta/recipes-connectivity/neard/neard"
SRC_URI += "\
	file://neard.in \
	file://Makefile.am-fix-parallel-issue.patch \
	file://Makefile.am-do-not-ship-version.h.patch \
	file://0001-Add-header-dependency-to-nciattach.o.patch \
"
LIC_FILES_CHKSUM = "\
	file://COPYING;md5=12f884d2ae1ff87c09e5b7ccc2c4ca7e \
	file://src/near.h;beginline=1;endline=20;md5=358e4deefef251a4761e1ffacc965d13 \
"

inherit autotools pkgconfig systemd update-rc.d bluetooth

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'systemd', d)}"

PACKAGECONFIG[systemd] = "--enable-systemd --with-systemdsystemunitdir=${systemd_unitdir}/system/ --with-systemduserunitdir=${systemd_unitdir}/user/,--disable-systemd"

EXTRA_OECONF += "--enable-tools"

# This would copy neard start-stop shell and test scripts
do_install_append() {
	if ${@bb.utils.contains('DISTRO_FEATURES', 'sysvinit', 'true', 'false', d)}; then
		install -d ${D}${sysconfdir}/init.d/
		sed "s:@installpath@:${libexecdir}/nfc:" ${WORKDIR}/neard.in \
			> ${D}${sysconfdir}/init.d/neard
		chmod 0755 ${D}${sysconfdir}/init.d/neard
	fi
}

RDEPENDS_${PN} = "dbus"

# Bluez & Wifi are not mandatory except for handover
RRECOMMENDS_${PN} = "\
	${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', '${BLUEZ}', '', d)} \
	${@bb.utils.contains('DISTRO_FEATURES', 'wifi','wpa-supplicant', '', d)} \
"

INITSCRIPT_NAME = "neard"
INITSCRIPT_PARAMS = "defaults 64"

SYSTEMD_SERVICE_${PN} = "neard.service"