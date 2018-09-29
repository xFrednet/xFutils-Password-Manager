; Modern UI
!include "MUI2.nsh"

; General
Name "XFutils Password Manager installer"
OutFile "Setup.exe"

InstallDir "$APPDATA\xFutils\PWManager"

RequestExecutionLevel user

Section
	
	# set the installation directory as the destination for the following actions
    SetOutPath $INSTDIR
	
	# create the uninstaller
    WriteUninstaller "$INSTDIR\uninstall.exe"
	
	File "out\artifacts\xFutils_Password_Manager_jar\xFutils Password Manager.jar"
	File "LICENSE.txt"
	File "icon.ico"
	
SectionEnd


; Installation
!insertmacro MUI_PAGE_LICENSE "LICENSE.txt"
Page directory
Page instfiles

Section
	CreateShortCut "$SMPROGRAMS\xFutils Password Manager.lnk" "$INSTDIR\xFutils Password Manager.jar" "" "$INSTDIR\icon.ico"
	CreateShortCut "$DESKTOP\xFutils Password Manager.lnk" "$INSTDIR\xFutils Password Manager.jar" "" "$INSTDIR\icon.ico"
SectionEnd

# uninstaller section start
Section "uninstall"
 
    # first, delete directory
	RMDir /r "$INSTDIR"
 
    # second, remove the link from the start menu
    Delete "$SMPROGRAMS\xFutils Password Manager.lnk"
 
# uninstaller section end
SectionEnd

; Uninstallation
UninstPage uninstConfirm
UninstPage instfiles
