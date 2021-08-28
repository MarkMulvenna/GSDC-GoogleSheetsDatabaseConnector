public class TEMP_TestObj {

    //Create a class for records in TEMP_Test Table, the isPassedToSheets boolean is not included as this will be set to 1 for every record after the script runs.

    int tempID;
    String tempString;
    String tempEmail;



    //Getters to allow for retrieval of items.
    public int getTempID() {
        return tempID;
    }

    public String getTempString() {
        return tempString;
    }

    public String getTempEmail() {
        return tempEmail;
    }

    //Setters to allow setting of items, will be used in DatabaseFunctions.java file.
    public void setTempID(int tempID) {
        this.tempID = tempID;
    }

    public void setTempString(String tempString) {
        this.tempString = tempString;
    }

    public void setTempEmail(String tempEmail) {
        this.tempEmail = tempEmail;
    }


}
