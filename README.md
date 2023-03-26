# BF2Sbx_SaveAndLoadServer
This litte Project will deploy a .war file with mvn using mvn clean package. 
This .war file can then be run on a tomcat server, currently developed for tomcat v10

The code provides a revival of the Shoutdown Sandbox Save and Load server.
With this, @netsavegroup and @netloadgroup can work again.

If you dont want to host your own server, don´t worry! I have done it already!
Simply change the ip adress in your Bf2 Sanboxserver as follows:

- stop your bf2 Sandbox server

- Navigate to <your_bf2_Server_home_directory>/bf2/mods/sandbox/python/game/gamemodes
  set the ip of the sbxNetwork.py file:
  host = "81.169.203.197"

- restart your server

Thats it!
