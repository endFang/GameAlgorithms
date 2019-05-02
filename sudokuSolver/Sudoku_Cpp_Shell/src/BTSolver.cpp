#include"BTSolver.hpp"

using namespace std;

// =====================================================================
// Constructors
// =====================================================================

BTSolver::BTSolver ( SudokuBoard input, Trail* _trail,  string val_sh, string var_sh, string cc )
: sudokuGrid( input.get_p(), input.get_q(), input.get_board() ), network( input )
{
	valHeuristics = val_sh;
	varHeuristics = var_sh;
	cChecks =  cc;

	trail = _trail;
}

// =====================================================================
// Consistency Checks
// =====================================================================

// Basic consistency check, no propagation done
bool BTSolver::assignmentsCheck ( void )
{
	for ( Constraint c : network.getConstraints() )
		if ( ! c.isConsistent() )
			return false;

	return true;
}

/**
 * Part 1 TODO: Implement the Forward Checking Heuristic
 *
 * This function will do both Constraint Propagation and check
 * the consistency of the network
 *
 * (1) If a variable is assigned then eliminate that value from
 *     the square's neighbors.
 *
 * Note: remember to trail.push variables before you change their domain
 * Return: true is assignment is consistent, false otherwise
 */
bool BTSolver::forwardChecking ( void )
{
	vector<Constraint *> cons = network.getModifiedConstraints();
	for (Constraint * c : cons)
	{
		for (Variable * v : c->vars)
		{
			if (v->isAssigned())
			{
				vector<Variable *> neighbors = network.getNeighborsOfVariable(v);
				for (Variable * n : neighbors)
				{
					if (n->getDomain().contains(v->getDomain().getValues()[0]))
					{
						trail->push(n);
						n->getDomain().remove(v->getDomain().getValues()[0]);
					}
				}
			}
		}
		if (!c->isConsistent())
			return false;
		return true;
	}
	return false;
}

/**
 * Part 2 TODO: Implement both of Norvig's Heuristics
 *
 * This function will do both Constraint Propagation and check
 * the consistency of the network
 *
 * (1) If a variable is assigned then eliminate that value from
 *     the square's neighbors.
 *
 * (2) If a constraint has only one possible place for a value
 *     then put the value there.
 *
 * Note: remember to trail.push variables before you change their domain
 * Return: true is assignment is consistent, false otherwise
 */
bool BTSolver::norvigCheck ( void )
{
	vector<Constraint *> cons = network.getModifiedConstraints();
	for (Constraint * c : cons)
	{
		for (Variable * v : c->vars)
		{
			if (v->isAssigned())
			{
				vector<Variable *> neighbors = network.getNeighborsOfVariable(v);
				for (Variable * n : neighbors)
				{
					if (n->getDomain().contains(v->getDomain().getValues()[0]))
					{
						trail->push(n);
						n->getDomain().remove(v->getDomain().getValues()[0]);
						if (n->getDomain().size() == 1)
						{
							trail->push(n);
							n->assignValue(n->getDomain().getValues()[0]);
						}
					}
				}
			}
		
		}
		
		if (!c->isConsistent())
			return false;
		return true;
	}
	return false;
}

/**
 * Optional TODO: Implement your own advanced Constraint Propagation
 *
 * Completing the three tourn heuristic will automatically enter
 * your program into a tournament.
 */
bool BTSolver::getTournCC ( void )
{
	return false;
}

// =====================================================================
// Variable Selectors
// =====================================================================

// Basic variable selector, returns first unassigned variable
Variable* BTSolver::getfirstUnassignedVariable ( void )
{
	for ( Variable* v : network.getVariables() )
		if ( !(v->isAssigned()) )
			return v;

	// Everything is assigned
	return nullptr;
}

/**
 * Part 1 TODO: Implement the Minimum Remaining Value Heuristic
 *
 * Return: The unassigned variable with the smallest domain
 */
Variable* BTSolver::getMRV ( void )
{
	int min = sudokuGrid.get_n();
	//find the minimum value
	for ( Variable* v : network.getVariables() )
	{
		if (v->isAssigned() == false && v->getDomain().size() <= min)
		{
			min = v->getDomain().size();
		}
	}

	for (Variable* v : network.getVariables())
	{
		if (v->isAssigned() == false && v->getDomain().size() == min)
		{
			return v;
		}
	}
	
	//nothing to return
	return nullptr;
}

/**
 * Part 2 TODO: Implement the Degree Heuristic
 *
 * Return: The unassigned variable with the most unassigned neighbors
 */
Variable* BTSolver::getDegree ( void )
{
	int max = 0;
	//find the max value
	for ( Variable* v : network.getVariables() )
	{
		//for each unassigne variale
		if (!v->isAssigned())
		{
			int unassignedCount = 0;
			vector< Variable* > neighbors = network.getNeighborsOfVariable(v);

			//find the most unassigned neighbors
			for (Variable* n : neighbors)
			{
				if (!n->isAssigned())
					++unassignedCount;
			}
			if (unassignedCount > max)
			{
				max = unassignedCount;
			}
		}
	}

	for (Variable* v : network.getVariables())
	{
		if (!v->isAssigned())
		{
			int unassignedCount = 0;
			vector< Variable* > neighbors = network.getNeighborsOfVariable(v);
			for (Variable* n : neighbors)
			{
				if (!n->isAssigned())
					++unassignedCount;
			}
			if (unassignedCount == max)
			{
				return v;
			}
		}
	}
	return nullptr;
}

/**
 * Part 2 TODO: Implement the Minimum Remaining Value Heuristic
 *                with Degree Heuristic as a Tie Breaker
 *
 * Return: The unassigned variable with the smallest domain and involved
 *             in the most constraints
 */
Variable* BTSolver::MRVwithTieBreaker ( void )
{
	int smallestDomain = sudokuGrid.get_n();
	//find the smallest domain
	for ( Variable* v : network.getVariables() )
	{
		if (!v->isAssigned() && v->getDomain().size() < smallestDomain)
		{
			smallestDomain = v->getDomain().size();
		}
	}

	//push unassigned variable with the smallest domain to buffer
	vector<Variable*> smallestDomainBuffer;
	for (Variable* v : network.getVariables())
	{
		if (!v->isAssigned() && v->getDomain().size() == smallestDomain)
		{
			smallestDomainBuffer.push_back(v);
		}
	}

	int mostContraints = 0;
	//find the max value
	
	for ( Variable* v : smallestDomainBuffer )
	{
		int unassignedCount = 0;
		vector< Variable* > neighbors = network.getNeighborsOfVariable(v);
		for (Variable* v2 : neighbors)
		{
			if (!v2->isAssigned())
				++unassignedCount;
		}
		if (unassignedCount > mostContraints)
			mostContraints = unassignedCount;
	}

	for (Variable* v : smallestDomainBuffer)
	{
		int unassignedCount = 0;
		vector< Variable* > neighbors = network.getNeighborsOfVariable(v);
		for (Variable* v2 : neighbors)
		{
			if (!v2->isAssigned())
				++unassignedCount;
		}
		if (unassignedCount == mostContraints)
		{
			return v;
		}
	}

	return nullptr;
}

/**
 * Optional TODO: Implement your own advanced Variable Heuristic
 *
 * Completing the three tourn heuristic will automatically enter
 * your program into a tournament.
 */
Variable* BTSolver::getTournVar ( void )
{
	return nullptr;
}

// =====================================================================
// Value Selectors
// =====================================================================

// Default Value Ordering
vector<int> BTSolver::getValuesInOrder ( Variable* v )
{
	vector<int> values = v->getDomain().getValues();
	sort( values.begin(), values.end() );
	return values;
}

/**
 * Part 1 TODO: Implement the Least Constraining Value Heuristic
 *
 * The Least constraining value is the one that will knock the least
 * values out of it's neighbors domain.
 *
 * Return: A list of v's domain sorted by the LCV heuristic
 *         The LCV is first and the MCV is last
 */

bool cmp(const pair<int,int> &p1,const pair<int,int> &p2)
{
	return p1.second<p2.second;
}

vector<int> BTSolver::getValuesLCVOrder ( Variable* v )
{
	vector<int> values = v->getDomain().getValues();
	vector<int> result;

	vector< pair<int, int> > tmp;
	vector< Variable* > neighbors = network.getNeighborsOfVariable(v);

	for (vector<int>::iterator it = values.begin(); it != values.end(); ++it)
	{
		int count = 0;
		for (int i=0; i!=neighbors.size(); ++i)
		{
			if (neighbors[i]->getDomain().contains(*it))
				++count;
		}
		tmp.push_back(make_pair(*it, count));
	}

	sort(tmp.begin(),tmp.end(),cmp); 

	for (vector< pair<int, int> >::iterator it=tmp.begin(); it!=tmp.end();++it)
		result.push_back(it->first);
	return result;
}

/**
 * Optional TODO: Implement your own advanced Value Heuristic
 *
 * Completing the three tourn heuristic will automatically enter
 * your program into a tournament.
 */
vector<int> BTSolver::getTournVal ( Variable* v )
{
	return vector<int>();
}

// =====================================================================
// Engine Functions
// =====================================================================

void BTSolver::solve ( void )
{
	if ( hasSolution )
		return;

	// Variable Selection
	Variable* v = selectNextVariable();

	if ( v == nullptr )
	{
		for ( Variable* var : network.getVariables() )
		{
			// If all variables haven't been assigned
			if ( ! ( var->isAssigned() ) )
			{
				cout << "Error" << endl;
				return;
			}
		}

		// Success
		hasSolution = true;
		return;
	}

	// Attempt to assign a value
	for ( int i : getNextValues( v ) )
	{
		// Store place in trail and push variable's state on trail
		trail->placeTrailMarker();
		trail->push( v );

		// Assign the value
		v->assignValue( i );

		// Propagate constraints, check consistency, recurse
		if ( checkConsistency() )
			solve();

		// If this assignment succeeded, return
		if ( hasSolution )
			return;

		// Otherwise backtrack
		trail->undo();
	}
}

bool BTSolver::checkConsistency ( void )
{
	if ( cChecks == "forwardChecking" )
		return forwardChecking();

	if ( cChecks == "norvigCheck" )
		return norvigCheck();

	if ( cChecks == "tournCC" )
		return getTournCC();

	return assignmentsCheck();
}

Variable* BTSolver::selectNextVariable ( void )
{
	if ( varHeuristics == "MinimumRemainingValue" )
		return getMRV();

	if ( varHeuristics == "Degree" )
		return getDegree();

	if ( varHeuristics == "MRVwithTieBreaker" )
		return MRVwithTieBreaker();

	if ( varHeuristics == "tournVar" )
		return getTournVar();

	return getfirstUnassignedVariable();
}

vector<int> BTSolver::getNextValues ( Variable* v )
{
	if ( valHeuristics == "LeastConstrainingValue" )
		return getValuesLCVOrder( v );

	if ( valHeuristics == "tournVal" )
		return getTournVal( v );

	return getValuesInOrder( v );
}

bool BTSolver::haveSolution ( void )
{
	return hasSolution;
}

SudokuBoard BTSolver::getSolution ( void )
{
	return network.toSudokuBoard ( sudokuGrid.get_p(), sudokuGrid.get_q() );
}

ConstraintNetwork BTSolver::getNetwork ( void )
{
	return network;
}
