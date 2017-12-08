# JTerminal
A standalone, customizable, command-line interface built with Swing.
## Basic Usage
### Creation & Setup:
Creating a new terminal:
> Terminal terminal = new Terminal( "window-title" );

Add command mappings:
> terminal.putCommand("key", ( )->{ \<YOUR CODE HERE\> });

Set a new default prompt:
> terminal.setDefaultPrompt( "prompt" );

Start the terminal:
> terminal.start( );

Full documentation to come.
