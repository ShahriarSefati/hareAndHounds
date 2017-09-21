//------------------------------------------------------------------------------//
// Hare and Hounds game, front end implementation by Shahriar Sefati (ssefati2)
// Date: 09/18/2017
//------------------------------------------------------------------------------//

package com.oose2017.ssefati2.hareandhounds;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
//import spark.Spark;
//import sun.awt.CausedFocusEvent;

// import Spark as 'static' so that it can be called easily (otherwise: Spark.ipAddress())
import static spark.Spark.*;

public class Bootstrap {
    public static final String IP_ADDRESS = "localhost";
    public static final int PORT = 8080;

    private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    public static void main(String[] args) throws Exception {

        //Specify the IP address and Port at which the server should be run
        ipAddress(IP_ADDRESS);
        port(PORT);

        // Specify the sub-directory from which to serve static resources (like html and css)
        staticFileLocation("/public");

        // Fire up the server (just for test, not necessary if we call a route later)
//        try {
//            init();
//        } catch (Exception failedStartup) {
//            logger.error("Failed at server startup", failedStartup);
//            throw new bootstrapException("Failed at server startup", failedStartup);
//        }

        //Create the model instance and then configure and start the web service
        try {
            GameService model = new GameService();
            new GameController(model);
        } catch(Exception ex){
            logger.error("Failed to create a HareHound instance. Aborting");
            throw new bootstrapException("Failed to create a HareHound instance", ex);
        }
    }

    //-----------------------------------------------------------------------------//
    // Helper Classes and Methods
    //-----------------------------------------------------------------------------//

    // Extending "Exception" class and calling its constructor by calling "super" (because it's the super class)
    public static class bootstrapException extends Exception{
        public bootstrapException(String message, Throwable cause) {super(message, cause);}
    }

}
