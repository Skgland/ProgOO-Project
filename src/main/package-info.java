/**
 * This package contains the Main class which contains the main method
 * that starts this program.
 *
 * This program uses the lwgjl and lwgjl-glfw library which are compile-time dependencies.
 * At runtime they and the corresponding dll's only need to be present when wanting to use the
 * ControllerInput controller.
 * Tested with Version 3.1.1
 *
 *  The program has several components.
 *  All components that are (part of) a Controller can be found in the de.webtwob.input package
 *  All components that are (part of) a View can be found in the de.webtwob.view package
 *      The view that implements the Lighthouse API can be found in the de.webtwob.view.lighthouse package
 *  All components that are (part of) a Model can be found in the de.webtwob.model package
 *  All interfaces and an enum can be found in the package de.webtwob.interfaces
 *  Everything relate to starting the Programm and the Main Thread can be found in the main package
 *
 * Decomposition is achieved by splitting the task of displaying multiple view,
 * handling multiple model and multiple Controller
 * The Controller split the task of taking and generating inputs,
 * therefor the AutoRun controller handles playing the game without human interaction
 * while GameLoop constantly runs cycle to update the games state
 * ControllerInput and KeyboardInput there while handle user input from the keyboard and an XBox360 Controller
 * The Model part is compost of:
 * The BasicGameModel which holds the game state and provides methods to update it.
 * The BasicMenuModel which tracks the state of the menu and provides functions to update it's state,
 * it uses the Classes in the de.webtwob.model.menu package to do it's task.
 * Last and may be least is the ModeModel it just keeps track of the Game being in game or in the Menu.
 * The View Part has two main Components
 * The Lighthouse view to display the game on the 28*14 Window front of the high-rise building
 * and The BasicView which displays the game on your normal Computer Monitor it delegates the task to
 * the GameField which displays the game if active or the MenuPanel if the menu os active
 *
 * We only used private instance variables, all the state information is only changed by method invocation
 * public class fields was used for constants
 * Most Methods are public because they need to be accessible from a public context
 * all other functions are private because they are for internal decomposition of tasks and not for external use.
 * */
package main;