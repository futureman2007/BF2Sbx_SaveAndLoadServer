package sbx.revive;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SbxGroupSaveController {

    private final String BASEPATH = "/~sandbox/network/server2.0";
    private final String SAVEDIR_HOME = "/home/bf2/sandbox/sandbox_netsaves";
    private final String FILE_ENDING = ".bf2sbg";
    private Group currentGroup=null;

    @PostMapping(value = BASEPATH + "/" + "sbx.saveGroup.php")
    public String saveGroup(@RequestBody String groupData) {
        String URLdecodedGroupDataIntoUTF8 = null;
            try {
                URLdecodedGroupDataIntoUTF8 = convertURLinoChars(groupData);
                if(URLdecodedGroupDataIntoUTF8 != null) {
                    currentGroup = new Group(
                        extractPlayernameFromReceivedData(URLdecodedGroupDataIntoUTF8),
                        extractPIDFromReceivedData(URLdecodedGroupDataIntoUTF8),
                        extractGroupnameFromReceivedData(URLdecodedGroupDataIntoUTF8),
                        extractDataFromReceivedData(URLdecodedGroupDataIntoUTF8)
                    );
                    saveGroupIntoFile(currentGroup, SAVEDIR_HOME);
                }//if
                
                
                
            }//try
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "Something went wrong sending the data to the server oO. Group not transmitted to Server and not Saved!";
            } catch (IOException e) {
                e.printStackTrace();
                return "Something went wrong saving the data. It was Received but could not be saved! Group not saved!";
            }

        return "Group "+ currentGroup.getGroupname() + " Saved for player " + currentGroup.getUsername();
    }//saveGroup
    
    @PostMapping(value = BASEPATH + "/" + "sbx.loadGroup.php")
    public String loadGroup(@RequestBody String request) {
        String URLdecodedGroupDataIntoUTF8 = null;
        try {
            URLdecodedGroupDataIntoUTF8 = convertURLinoChars(request);
            if(URLdecodedGroupDataIntoUTF8 != null) {
                currentGroup = new Group(
                    extractPlayernameFromReceivedData(URLdecodedGroupDataIntoUTF8),
                    extractPIDFromReceivedData(URLdecodedGroupDataIntoUTF8),
                    extractGroupnameFromReceivedData(URLdecodedGroupDataIntoUTF8),
                    null
                );
                loadGroupDataFromFile(currentGroup, SAVEDIR_HOME);
            }//if    
        }//try
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "Something went wrong sending the data to the server oO. Group not transmitted to Server and not Saved!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Something went wrong saving the data. It was Received but could not be saved! Group not saved!";
        }

        return currentGroup.getGroupdata();
    }//loadGroup

    //TODO REFACTOR: Outsource it into its own Class: GroupFileManager
    private synchronized void saveGroupIntoFile(Group g, String fileHomedir) throws IOException{
        final String FILENAME = g.getUsername() + "_" + g.getPid() + "-" + g.getGroupname() + FILE_ENDING;

        RandomAccessFile stream = new RandomAccessFile(fileHomedir + "/" + FILENAME, "rw");
        FileChannel channel = stream.getChannel();
        byte[] strBytes = g.getGroupdata().getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(strBytes.length);

        buffer.put(strBytes);
        buffer.flip();
        channel.write(buffer);
        stream.close();
        channel.close();
    }// writeStrintIntoFile


         //TODO REFACTOR: Outsource it into its own Class: GroupFileManager
    private void loadGroupDataFromFile(Group g, String fileHomeDir) throws IOException{
        String filename = g.getUsername() + "_" + g.getPid() + "-" + g.getGroupname() + FILE_ENDING;
        String fileFullPath = fileHomeDir + "/" + filename;
        //NOTE: We saved the group using UTF-8 (see convertURLinoChars)! Therefore we read the file using  UTF-8 Charset
        BufferedReader reader = Files.newBufferedReader(
            FileSystems.getDefault().getPath(fileFullPath), 
            Charset.forName("UTF-8")
            );
        String line = null;
        StringBuffer sbuff = new StringBuffer(1024);
        while ((line = reader.readLine()) != null){
            sbuff.append(line);    
        }
        g.setGroupdata(sbuff.toString());

    }//loadGroupDataFromFile
    
    private String convertURLinoChars(String urlString) throws UnsupportedEncodingException{
        return URLDecoder.decode(urlString, StandardCharsets.UTF_8.name());
    }
    
    private String extractPlayernameFromReceivedData(String netsavegroupData){
        String[] data = netsavegroupData.split("&");
        String name=data[0].split("=")[1];

        System.out.println("DEBUG: " + name );
        return name; 
    }

    private String extractPIDFromReceivedData(String netsavegroupdata){
        String[] data = netsavegroupdata.split("&");
        String pid=data[1].split("=")[1];

        System.out.println("DEBUG: " + pid );
        return pid; 
    }

    private String extractGroupnameFromReceivedData(String netsavegroupdata){
        String[] data = netsavegroupdata.split("&");
        String groupname=data[2].split("=")[1];

        System.out.println("DEBUG: " + groupname );
        return groupname; 
    }

    private String extractDataFromReceivedData(String netsavegroupData){
        String[] data = netsavegroupData.split("&");

        String dataContent = data[3].split("=")[1];
        System.out.println("DEBUG: " + dataContent);
        return dataContent; 
    }

}//HelloController
