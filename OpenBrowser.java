package abalone;

import javax.swing.JOptionPane;

public class OpenBrowser {

    public static void openURL(String url) {
        String sysName = System.getProperty("os.name");
        try {
            if (sysName.startsWith("Windows")) {
                Runtime.getRuntime().exec(
                        "rundll32 url.dll,FileProtocolHandler " + url);
            } else {
                String[] browsers = {"firefox", "opera", "chrorme", "brave", "internet explorer", "edge"};
                String browser = null;
                for (int count = 0; count < browsers.length && browser == null; count++) {
                    if (Runtime.getRuntime().exec(
                            new String[]{"which", browsers[count]})
                            .waitFor() == 0) {
                        browser = browsers[count];
                    }
                }
                Runtime.getRuntime().exec(new String[]{browser, url});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Can not open browser"
                    + ":\n" + e.getLocalizedMessage());
        }
    }
}
