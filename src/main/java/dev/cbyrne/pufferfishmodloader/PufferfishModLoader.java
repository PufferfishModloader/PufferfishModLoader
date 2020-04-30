package dev.cbyrne.pufferfishmodloader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class PufferfishModLoader {
    public static final PufferfishModLoader INSTANCE = new PufferfishModLoader();
    public final Logger logger = LogManager.getLogger("PufferfishModLoader");
    public File gameDir;

    public void preInit() {
        logger.info("PML Started!");

    }
}
