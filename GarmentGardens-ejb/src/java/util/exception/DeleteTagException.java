/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author wong
 */
public class DeleteTagException extends Exception{

    /**
     * Creates a new instance of <code>DeleteTagException</code> without detail
     * message.
     */
    public DeleteTagException() {
    }

    /**
     * Constructs an instance of <code>DeleteTagException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteTagException(String msg) {
        super(msg);
    }
}
