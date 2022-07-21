package main.java.com.chapter4.example1;

import java.io.File;
import java.io.IOException;

interface Importer {
    Document importFile(File file) throws IOException;
}