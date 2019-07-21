#include "stdafx.h"
#include <iostream>
#include <string>
using namespace std;

// Variables
int board[3][3] = {{0,0,0}, {0,0,0}, {0,0,0}};
int winner = 0;
int movePos = -1;
bool player1Turn = true;
int turns = 0;

//////////////////////////////////////////////////////////////////////
//
// Draw the board including ech player's signs
//
//////////////////////////////////////////////////////////////////////

void drawBoard() {
	for (int i = 0; i < 3; i++) {			// Iterate over board rows
		cout << "|";						// Print "|"
		for (int j = 0; j < 3; j++) {		// Iterate over columns
			string space;					// Declare space string
			if (board[i][j] == 0) {			
				space = "_";				// If space is free, print "_"
			} else if (board[i][j] == 1) {
				space = "X";				// If player 1 has space, print "X"
			} else {
				space = "O";				// Else print "O" (player 2 has space)
			}
			cout << " " << space << " |";	// Print each space followed by a "|"
		}
		cout << "\n";						// Go to next row
	}
}

//////////////////////////////////////////////////////////////////////
//
// Check board to see, if any player won the game yet
//
//////////////////////////////////////////////////////////////////////

void checkWinner() {
	for (int i = 0; i < 3; i++) {
		
		// Check rows
		if (board[i][0] == board[i][1] &&
			board[i][1] == board[i][2]) {
			if (board[i][0] == 1) {
				winner = 1;
			} else if (board[i][0] == 2) {
				winner = 2;
			}
		}

		// Check columns
		if (board[0][i] == board[1][i] &&
			board[1][i] == board[2][i]) {
			if (board[0][i] == 1) {
				winner = 1;
			}
			else if (board[0][i] == 2) {
				winner = 2;
			}
		}
	}

	// Check first diagonal
	if (board[0][0] == board[1][1] &&
		board[1][1] == board[2][2]) {
		if (board[0][0] == 1) {
			winner = 1;
		}
		else if (board[0][0] == 2) {
			winner = 2;
		}
	}

	// Check second diagonal
	if (board[0][2] == board[1][1] &&
		board[1][1] == board[2][0]) {
		if (board[0][2] == 1) {
			winner = 1;
		}
		else if (board[0][2] == 2) {
			winner = 2;
		}
	}
}

//////////////////////////////////////////////////////////////////////
//
// Read player input, then validate and process it
//
//////////////////////////////////////////////////////////////////////

void readInput() {
	
	// Get player sign
	int sign;
	if (player1Turn) {
		sign = 1;
	} else {
		sign = 2;
	}

	// Get, validate, & process input
	while (movePos < 0 || movePos > 8) {
		cout << "Choice: ";
		cin >> movePos;

		// Validate input data type
		if (!cin) {
			cin.clear();
			cin.ignore(numeric_limits<streamsize>::max(), '\n');
		}

		// Get player sign (1 or 2)
		int sign;
		if (player1Turn) {
			sign = 1;
			player1Turn = false;
		} else {
			sign = 2;
			player1Turn = true;
		}

		// Process input
		if (movePos == 0) {
			if (board[0][0] == 0) {
				board[0][0] = sign;
			} else {
				cout << "Already taken.\n";
				movePos = -1;
			}
		} else if (movePos == 1) {
			if (board[0][1] == 0) {
				board[0][1] = sign;
			} else {
				cout << "Already taken.\n";
				movePos = -1;
			}
		} else if (movePos == 2) {
			if (board[0][2] == 0) {
				board[0][2] = sign;
			} else {
				cout << "Already taken.\n";
				movePos = -1;
			}
		} else if (movePos == 3) {
			if (board[1][0] == 0) {
				board[1][0] = sign;
			} else {
				cout << "Already taken.\n";
				movePos = -1;
			}
		} else if (movePos == 4) {
			if (board[1][1] == 0) {
				board[1][1] = sign;
			} else {
				cout << "Already taken.\n";
				movePos = -1;
			}
		} else if (movePos == 5) {
			if (board[1][2] == 0) {
				board[1][2] = sign;
			} else {
				cout << "Already taken.\n";
				movePos = -1;
			}
		} else if (movePos == 6) {
			if (board[2][0] == 0) {
				board[2][0] = sign;
			} else {
				cout << "Already taken.\n";
				movePos = -1;
			}
		} else if (movePos == 7) {
			if (board[2][1] == 0) {
				board[2][1] = sign;
			} else {
				cout << "Already taken.\n";
				movePos = -1;
			}
		} else if (movePos == 8) {
			if (board[2][2] == 0) {
				board[2][2] = sign;
			} else {
				cout << "Already taken.\n";
				movePos = -1;
			}
		} else {
			cout << "Invalid move.\n";
			movePos = -1;
		}
	}
	movePos = -1;							// Reset variable after move
}
//////////////////////////////////////////////////////////////////////
//
// Display input prompt, then read input
//
//////////////////////////////////////////////////////////////////////

void promptInput() {
	cout << "please choose one of the following free spaces:\n";
	if (board[0][0] == 0)
		cout << "Top Left: 0\n";
	if (board[0][1] == 0)
		cout << "Top Center: 1\n";
	if (board[0][2] == 0)
		cout << "Top Right: 2\n";
	if (board[1][0] == 0)
		cout << "Mid Left: 3\n";
	if (board[1][1] == 0)
		cout << "Mid Center: 4\n";
	if (board[1][2] == 0)
		cout << "Mid Right: 5\n";
	if (board[2][0] == 0)
		cout << "Bottom Left: 6\n";
	if (board[2][1] == 0)
		cout << "Bottom Center: 7\n";
	if (board[2][2] == 0)
		cout << "Bottom Right: 8\n\n";
	readInput();
}
//////////////////////////////////////////////////////////////////////
//
// Main
//
//////////////////////////////////////////////////////////////////////

int main()
{
	cout << "Tic Tac Toe!\n\n";				// Display title

	// Game Loop
	while (winner == 0) {					// Loop while there's no winner
		drawBoard();						// Draw the board
		if (player1Turn) {					// Player 1 moves
			cout << "\nPlayer 1 ";
			promptInput();
		} else {							// Player 2 moves
			cout << "\nPlayer 2 ";
			promptInput();
		}
		checkWinner();						// Check if someone won
		turns++;							// Next turn
		if (turns >= 9) {
			break;							// End after 9 turns (needed for tie)
		}
	}

	// Display winner
	if (winner == 1) {
		cout << "\nPlayer 1 won the game!\n";
	} else if (winner == 2) {
		cout << "\nPlayer 2 won the game!\n";
	} else {
		cout << "\nTie!\n";
	}
	drawBoard();							// Draw the final board
	return 0;								// Exit game
}