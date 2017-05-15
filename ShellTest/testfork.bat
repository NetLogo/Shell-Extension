# This procedure uses two pings to a non-existant ip address
# with 30 seconds between them in order to test that the 
# bat file is indeed running independently of NetLogo.
# It writes to the user's home directory as we can be pretty 
# sure the user has write access to it.

ping 1.1.1.1 -n 1 -w 30000 > nul
dir > %USERPROFILE%/testfork.txt
exit