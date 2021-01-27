package com.example.sleeperstudent;

import android.content.Context;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class User
{

    private static final String FILE_PATH = "userData.txt";
    private static final char DATA_SEPERATOR = (int) 7;

    //add more fields down here
    private String userName;
    private int age;



    //Setters
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    public void setAge(int age)
    {
        this.age = age;
    }

    //Getters
    public String getUserName()
    {
        return this.userName;
    }
    public int getAge()
    {
        return age;
    }


    /*************************************************************************************
     *        Function: writeToFile
     *       Variables: Context: Application contectual info.
     *  Post Condition: Writes user data to the system.
     *            Note: To pass context to this function, pass the "this" keyword. If you are
     *                  accessing this object in a listener function, pass view.getContext();
     ************************************************************************************/
    public void writeToFile(Context context)
    {
        FileOutputStream out = null;
        try
        {
            File folder = new File(context.getFilesDir() + "/userData");
            File txtFile = new File(folder.getAbsolutePath() + "/" + FILE_PATH);

            if(!txtFile.exists())
                folder.mkdir();
            if(!txtFile.exists())
                txtFile.createNewFile();

            out = new FileOutputStream(txtFile);
            String printName = "Name: " + DATA_SEPERATOR + userName + "\n";
            String printAge = "Age: " + DATA_SEPERATOR + Integer.toString(age) + "\n";

            out.write(printName.getBytes());
            out.write(printAge.getBytes());

            out.flush();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                out.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /*************************************************************************************
     *        Function: InputData
     *       Variables: Context: Application contectual info.
     *  Post Condition: Reads data from file to the object
     *            Note: To pass context to this function, pass the "this" keyword.
     ************************************************************************************/
    public void inputData(Context context)
    {
        File folder = new File(context.getFilesDir() + "/userData");
        File txtFile = new File(folder.getAbsolutePath() + "/" + FILE_PATH);

        try
        {
            FileInputStream fis = new FileInputStream(txtFile);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String input;
            String parseInput[];
            while ((input = br.readLine()) != null)
            {
                parseInput = input.split("" + DATA_SEPERATOR);
                switch (parseInput[0])
                {
                    case "Name: ":
                        userName = parseInput[1];
                        break;
                    case "Age: ":
                        age = Integer.parseInt(parseInput[1]);
                        break;
                }

            }
            br.close();
            in.close();
            fis.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }




}
