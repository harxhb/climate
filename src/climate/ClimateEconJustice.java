package climate;

import java.util.ArrayList;

/**
 * This class contains methods which perform various operations on a layered 
 * linked list structure that contains USA communities Climate and Economic information.
 */

public class ClimateEconJustice {

    private StateNode firstState;
    
    /*
    * Constructor
    */
    public ClimateEconJustice() {
        firstState = null;
    }

    /*
    * Get method to retrieve instance variable firstState
    * 
    * @return firstState
    */ 
    public StateNode getFirstState () {
        // DO NOT EDIT THIS CODE
        return firstState;
    }

    /**
     * Creates 3-layered linked structure consisting of state, county, 
     * and community objects by reading in CSV file provided.
     * 
     * @param inputFile, the file read from the Driver to be used for
     * @return void
     * 
     * **** DO NOT EDIT *****
     */
    public void createLinkedStructure ( String inputFile ) {
        
        // DO NOT EDIT THIS CODE
        StdIn.setFile(inputFile);
        StdIn.readLine();
        
        // Reads the file one line at a time
        while ( StdIn.hasNextLine() ) {
            // Reads a single line from input file
            String line = StdIn.readLine();
            // IMPLEMENT these methods
            addToStateLevel(line);
            addToCountyLevel(line);
            addToCommunityLevel(line);
        }
    }

    /*
    * Adds a state to the first level of the linked structure.
    * Do nothing if the state is already present in the structure.
    * 
    * @param inputLine a line from the input file
    */
    public void addToStateLevel ( String inputLine ) {

        String[] data = inputLine.split(",");
        String stateN = data[2].trim();
        StateNode current = firstState;
        
        while (current != null) 
        {
            if (current.getName().equals(stateN)) 
            {
                return;
            }
            if (current.getNext() == null)
            {
                break;
            }
            current = current.getNext();
        }
        if (firstState == null)
        {
            firstState = new StateNode(stateN, null, null);
        } 
        else 
        {
            current.setNext(new StateNode(stateN, null, null));
        }
    }

    /*
    * Adds a county to a state's list of counties.
    * 
    * Access the state's list of counties' using the down pointer from the State class.
    * Do nothing if the county is already present in the structure.
    * 
    * @param inputFile a line from the input file
    */
    public void addToCountyLevel ( String inputLine ) {

        String[] data = inputLine.split(",");
        String stateN = data[2].trim();
        String countyN = data[1].trim();
        StateNode ptr = firstState;
        
        while (ptr != null)
        {
            if(ptr.getName().equals(stateN))
            {
                CountyNode firstCounty = null;
                for(CountyNode bron = ptr.getDown(); bron != null; bron = bron.getNext())
                {
                    if(bron.getName().equals(countyN))
                    {
                        return;
                    }
                    firstCounty = bron;

                }
                if(firstCounty == null)
                {
                    ptr.setDown(new CountyNode(countyN, null, null));
                }
                else
                {
                    firstCounty.setNext(new CountyNode(countyN, null, null));
                }
            }
            ptr = ptr.getNext();
        }
    
    }

    /*
    * Adds a community to a county's list of communities.
    * 
    * Access the county through its state
    *      - search for the state first, 
    *      - then search for the county.
    * Use the state name and the county name from the inputLine to search.
    * 
    * Access the state's list of counties using the down pointer from the StateNode class.
    * Access the county's list of communities using the down pointer from the CountyNode class.
    * Do nothing if the community is already present in the structure.
    * 
    * @param inputFile a line from the input file
    */
    public void addToCommunityLevel ( String inputLine ) {

        String[] data = inputLine.split(",");
        String stateN = data[2].trim();
        String countyN = data[1].trim();
        String communityN = data[0].trim();
        double percentAfrican = Double.parseDouble(data[3]);
        double percentNative = Double.parseDouble(data[4]);
        double percentAsian = Double.parseDouble(data[5]);
        double percentWhite = Double.parseDouble(data[8]);
        double percentHispanic = Double.parseDouble(data[9]);
        String disadvantaged = data[19].trim();
        double pmLevel = Double.parseDouble(data[49]);
        double floodChance = Double.parseDouble(data[37]);
        double povertyLine = Double.parseDouble(data[121]);

        StateNode ptr = firstState;
        
        /*
         * Percent African American: index 3
        Percent Native: index 4
        Percent Asian: index 5
        Percent White: index 8
        Percent Hispanic: index 9
        Disadvantaged: index 19
        PM Level: index 49
        Chance of Flood: index 37
        Poverty Line: index 121
         */
        while (ptr != null)
        {
            if(ptr.getName().equals(stateN))
            {
                for(CountyNode bron = ptr.getDown(); bron != null; bron = bron.getNext())
                {
                    if(bron.getName().equals(countyN))
                    {
                        CommunityNode firstCommunity = null;
                        for(CommunityNode steph = bron.getDown(); steph != null; steph = steph.getNext())
                        {
                            if(steph.getName().equals(communityN))
                            {
                                return;
                            }
                            firstCommunity = steph;
                        }
                        Data lebron = new Data(percentAfrican, percentNative, percentAsian, percentWhite, percentHispanic, disadvantaged, pmLevel, floodChance,povertyLine);
                        if(firstCommunity == null)
                        {
                            bron.setDown(new CommunityNode(communityN, null, lebron));
                            return;
                        }
                        else
                        {
                            firstCommunity.setNext(new CommunityNode(communityN, null, lebron));
                            return;
                        }
                    }
                }
            }
            ptr = ptr.getNext();
        }

    }

    /**
     * Given a certain percentage and racial group inputted by user, returns
     * the number of communities that have that said percentage or more of racial group  
     * and are identified as disadvantaged
     * 
     * Percentages should be passed in as integers for this method.
     * 
     * @param userPrcntage the percentage which will be compared with the racial groups
     * @param race the race which will be returned
     * @return the amount of communities that contain the same or higher percentage of the given race
     */
    public int disadvantagedCommunities ( double userPrcntage, String race ) {
        int counter = 0;
        for(StateNode state = firstState; state != null; state = state.getNext())
        {
            for(CountyNode county = state.getDown(); county != null; county = county.getNext())
            {
                for(CommunityNode community = county.getDown(); community != null; community = community.getNext())
                {
                    if(race.equals("Hispanic American") && community.getInfo().getAdvantageStatus().equals("True") && community.getInfo().getPrcntHispanic() * 100 >= userPrcntage)
                    {
                        counter++;
                    }
                    if(race.equals("White American") && community.getInfo().getAdvantageStatus().equals("True") && community.getInfo().getPrcntWhite() * 100 >= userPrcntage)
                    {
                        counter++;
                    }
                    if(race.equals("Asian American") && community.getInfo().getAdvantageStatus().equals("True") && community.getInfo().getPrcntAsian() * 100 >= userPrcntage)
                    {
                        counter++;
                    }
                    if(race.equals("Native American") && community.getInfo().getAdvantageStatus().equals("True") && community.getInfo().getPrcntNative() * 100 >= userPrcntage)
                    {
                        counter++;
                    }
                    if(race.equals("African American") && community.getInfo().getAdvantageStatus().equals("True") && community.getInfo().getPrcntAfricanAmerican() * 100 >= userPrcntage)
                    {
                        counter++;
                    }
                    
                }
            }
        }
        return counter; // replace this line
    }

    /**
     * Given a certain percentage and racial group inputted by user, returns
     * the number of communities that have that said percentage or more of racial group  
     * and are identified as non disadvantaged
     * 
     * Percentages should be passed in as integers for this method.
     * 
     * @param userPrcntage the percentage which will be compared with the racial groups
     * @param race the race which will be returned
     * @return the amount of communities that contain the same or higher percentage of the given race
     */
    public int nonDisadvantagedCommunities ( double userPrcntage, String race ) {

        int counter = 0;
        for(StateNode state = firstState; state != null; state = state.getNext())
        {
            for(CountyNode county = state.getDown(); county != null; county = county.getNext())
            {
                for(CommunityNode community = county.getDown(); community != null; community = community.getNext())
                {
                    if(race.equals("Hispanic American") && community.getInfo().getAdvantageStatus().equals("False") && community.getInfo().getPrcntHispanic() * 100 >= userPrcntage)
                    {
                        counter++;
                    }
                    if(race.equals("White American") && community.getInfo().getAdvantageStatus().equals("False") && community.getInfo().getPrcntWhite() * 100 >= userPrcntage)
                    {
                        counter++;
                    }
                    if(race.equals("Asian American") && community.getInfo().getAdvantageStatus().equals("False") && community.getInfo().getPrcntAsian() * 100 >= userPrcntage)
                    {
                        counter++;
                    }
                    if(race.equals("Native American") && community.getInfo().getAdvantageStatus().equals("False") && community.getInfo().getPrcntNative() * 100 >= userPrcntage)
                    {
                        counter++;
                    }
                    if(race.equals("African American") && community.getInfo().getAdvantageStatus().equals("False") && community.getInfo().getPrcntAfricanAmerican() * 100 >= userPrcntage)
                    {
                        counter++;
                    }
                    
                }
            }
        }
        return counter;
    }
    
    /** 
     * Returns a list of states that have a PM (particulate matter) level
     * equal to or higher than value inputted by user.
     * 
     * @param PMlevel the level of particulate matter
     * @return the States which have or exceed that level
     */ 
    public ArrayList<StateNode> statesPMLevels ( double PMlevel ) {

        ArrayList<StateNode> lebron = new ArrayList<>();
        for(StateNode state = firstState; state != null; state = state.getNext())
        {
            for(CountyNode county = state.getDown(); county != null; county = county.getNext())
            {
                for(CommunityNode community = county.getDown(); community != null; community = community.getNext())
                {
                    if(community.getInfo().getPMlevel() >= PMlevel)
                    {
                        if(!lebron.contains(state))
                        {
                            lebron.add(state);
                        }
                    }
                }
            }
        }
        return lebron; // replace this line
    }

    /**
     * Given a percentage inputted by user, returns the number of communities 
     * that have a chance equal to or higher than said percentage of
     * experiencing a flood in the next 30 years.
     * 
     * @param userPercntage the percentage of interest/comparison
     * @return the amount of communities at risk of flooding
     */
    public int chanceOfFlood ( double userPercntage ) {

        int twinemmm = 0;
        for(StateNode state = firstState; state != null; state = state.getNext())
        {
            for(CountyNode county = state.getDown(); county != null; county = county.getNext())
            {
                for(CommunityNode community = county.getDown(); community != null; community = community.getNext())
                {
                    if(community.getInfo().getChanceOfFlood() >= userPercntage)
                    {
                        twinemmm++;
                    }
                }
            }
        }        
        return twinemmm; // replace this line
    }

    /** 
     * Given a state inputted by user, returns the communities with 
     * the 10 lowest incomes within said state.
     * 
     *  @param stateName the State to be analyzed
     *  @return the top 10 lowest income communities in the State, with no particular order
    */
    public ArrayList<CommunityNode> lowestIncomeCommunities ( String stateName ) {

        ArrayList<CommunityNode> mj = new ArrayList<>();
        for(StateNode bron = firstState; bron != null; bron = bron.getNext())
        {
            if(bron.getName().equals(stateName))
            {
                for(CountyNode steph = bron.getDown(); steph != null; steph = steph.getNext())
                {
                    for(CommunityNode shai = steph.getDown(); shai != null; shai = shai.getNext())
                    {   
                        if(mj.size() < 10)
                        {
                            mj.add(shai);
                        }
                        else
                        {
                            CommunityNode min = mj.get(0);
                            for(int i = 0; i < 10; i++)
                            {
                                if(mj.get(i).getInfo().getPercentPovertyLine() < min.getInfo().getPercentPovertyLine())
                                {
                                    min = mj.get(i);
                                }
                            }
                            if(shai.getInfo().getPercentPovertyLine() > min.getInfo().getPercentPovertyLine())
                            {
                                mj.set(mj.indexOf(min), shai);
                            }
                        }
                    }
                }
            }
        }
        return mj; // replace this line
    }
}
    