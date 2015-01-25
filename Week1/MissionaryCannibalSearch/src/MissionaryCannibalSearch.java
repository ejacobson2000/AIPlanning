import java.util.HashSet;
import java.util.Set;
import java.util.Vector;


public class MissionaryCannibalSearch 
{
  class Bank
  {
    int _missionaries = 0;
    int _cannibals = 0;
    boolean _boat = false;
    
    Bank() {}
    Bank(int m, int c, boolean b)
    {
      _missionaries = m;
      _cannibals = c;
      _boat = b;
    }
    Bank(Bank bank)
    {
      _missionaries = bank._missionaries;
      _cannibals = bank._cannibals;
      _boat = bank._boat;
    }
    
    public boolean isValid()
    {
      if (_missionaries < 0 || _missionaries > 3)
        return false;
      if (_cannibals < 0 || _cannibals > 3)
        return false;
      
      return true;
    }
    
    public String toString()
    {
      if (_boat)
        return new String(Integer.toString(_missionaries) + "," + Integer.toString(_cannibals) + ",b");
      else
        return new String(Integer.toString(_missionaries) + "," + Integer.toString(_cannibals));
    }
  }
  
  class State
  {
    Bank _leftBank = null;
    Bank _rightBank = null;
    State _parentState = null;
    
    State() {}
    
    public void initialize()
    {
      _leftBank = new Bank(3,3,true);
      _rightBank = new Bank(0,0,false);
    }
    
    public void initialize(State state)
    {
      _parentState = state;
      _leftBank = new Bank(state._leftBank);
      _rightBank = new Bank(state._rightBank);
    }

    public void moveBoat(int missionaries, int cannibals)
    {
      if (_leftBank._boat)
      {
        _leftBank._boat = false;
        _rightBank._boat = true;
        _leftBank._missionaries -= missionaries;
        _leftBank._cannibals -= cannibals;
        _rightBank._missionaries += missionaries;
        _rightBank._cannibals += cannibals;
      }
      else
      {
        _leftBank._boat = true;
        _rightBank._boat = false;
        _leftBank._missionaries += missionaries;
        _leftBank._cannibals += cannibals;
        _rightBank._missionaries -= missionaries;
        _rightBank._cannibals -= cannibals;        
      }
    }
    public boolean isValid()
    {
      // Don't allow cannibals to out number missionaries  
      if ((_leftBank._missionaries != 0 && _leftBank._cannibals > _leftBank._missionaries) ||
          (_rightBank._missionaries != 0 && _rightBank._cannibals > _rightBank._missionaries))
        return false;

      if (!_leftBank.isValid() || !_rightBank.isValid())
        return false;
      
      if (_leftBank._boat == _rightBank._boat)
        return false;
      
      return true;
    }
    
    public boolean isGoal()
    {
      if (_rightBank._cannibals == 3 && _rightBank._missionaries == 3)
        return true;
      
      return false;
    }
    
    public String toString()
    {
      return new String("L{" + _leftBank.toString() + "},R{" + _rightBank.toString() + "}");
    }
  }
  
  State _initialState = new State();
  HashSet<String> _visitedStates = new HashSet<String>();
  
	public static void main(String[] args) 
	{
	  MissionaryCannibalSearch mcSearch = new MissionaryCannibalSearch();
	  mcSearch._initialState.initialize();
	  
	  String path = mcSearch.search(mcSearch._initialState);
	  
	  if (path.isEmpty())
	    System.out.println("No path found to goal.");
	  else
	    System.out.println(path);
	}
	
	String search(State state)
	{
	  if (state.isGoal())
	    return getPath(state);
	  
	  _visitedStates.add(state.toString());
	  
	  // Get the list of valid states from this one
	  Vector<State> fringe = generateStates(state);
	  if (fringe.isEmpty())
	    return new String("");
	  
	  for (int i = 0; i < fringe.size(); i++)
	  {
	    String path = search(fringe.get(i));
	    if (!path.isEmpty())
	      return path;
	  }
	  
	  return new String("");
	}

	Vector<State> generateStates(State state)
	{
	  Vector<State> states = new Vector<State>();
	  
	  // There are at most 5 possible actions
	  for (int i = 0; i < 5; i++)
	  {
	    // Create a new state and initialize it from the current state
	    State newState = new State();
	    newState.initialize(state);
	    
	    switch(i)
	    {
	    case 0:
	      newState.moveBoat(2,0);
	      break;
	    case 1:
        newState.moveBoat(1,0);
        break;
	    case 2:
	      newState.moveBoat(1,1);
	      break;
	    case 3:
	      newState.moveBoat(0,1);
	      break;
	    case 4:
	      newState.moveBoat(0,2);
	      break;
	    }

	    // Throw out invalid states
      if (!newState.isValid())
        continue;
      
      // Avoid any states we've already been through
	    if (_visitedStates.contains(newState.toString()))
	      continue;
	    
	    states.add(newState);
	  }
	  
	  return states;
	}
	
	String getPath(State state)
	{
	  if (state == null)
	    return new String("");
	  
	  return getPath(state._parentState) + " " + state.toString(); 
	}
}
