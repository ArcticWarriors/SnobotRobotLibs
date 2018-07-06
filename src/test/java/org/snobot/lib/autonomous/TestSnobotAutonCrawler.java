package org.snobot.lib.autonomous;

import java.io.File;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestSnobotAutonCrawler
{

    @Test
    public void testCrawler()
    {
        SnobotAutonCrawler crawler = new SnobotAutonCrawler("");
        ObservableSendableChooser<File> chooser = crawler.loadAutonFiles("testFiles", "parallel.auto");

        String selectedPath = chooser.getSelected().getAbsolutePath();
        selectedPath = selectedPath.replaceAll("\\\\", "/");

        Assertions.assertTrue(selectedPath.endsWith("/testFiles/autonParser/parallel.auto"));
    }

    @Test
    public void testCrawlerWithIgnore()
    {
        SnobotAutonCrawler crawler = new SnobotAutonCrawler("ignored_folder");
        ObservableSendableChooser<File> chooser = crawler.loadAutonFiles("testFiles");

        Assertions.assertNotNull(chooser);
    }

    @Test
    public void testInvalidDirectory()
    {
        SnobotAutonCrawler crawler = new SnobotAutonCrawler("");
        ObservableSendableChooser<File> chooser = crawler.loadAutonFiles("doesnt_exist");

        Assertions.assertNotNull(chooser);
    }
}
