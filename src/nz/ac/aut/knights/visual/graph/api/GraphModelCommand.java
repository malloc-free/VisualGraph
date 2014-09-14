/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.knights.visual.graph.api;

/**
 *
 * @author michael
 */
public enum GraphModelCommand {
    M_VERTEX_LIST, 
    M_VERTEX_NO_,
    M_VERTEX_CLICKED,
    M_VERTEX_NAME,
    M_VERTEX_X_POS,
    M_VERTEX_Y_POS,
    M_VERTEX_MOVED,
    M_R_CLICK_MENU_ACTION,
    OPEN_FILE,
    OPEN_FILE_TYPE,
    OPEN_FILE_NAME, 
    CLEAR_ALL_MODEL,
    DIRECTED,
    UNDIRECTED,
    RANDOM,
    FIND_PATH_FINISH,
    FIND_PATH_LIST,
    FIND_EXECUTE,
    RETURN_COMMAND,
    FIND_PATH_START,
    M_DELETE_VERTEX,
    M_DELETE_EDGE,
    M_VERTEX_ADD,
    M_EDGE_ADD;
}
