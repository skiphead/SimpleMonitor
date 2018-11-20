public class Main extends Thread {
    @Override
    public void run() {
        monitUrl test = new monitUrl();
        while (true) {
            try {
                Thread.sleep(20000);
            } catch (InterruptedException exc) {
                System.out.println(exc + " error Thread Scan port");
            }
            try {
                test.test();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

class Run {

    public static void main(String[] args) throws Exception {
        HttpServer run = new HttpServer();
        Main mA = new Main();
        mA.start();

            run.httpServer();
    }
}