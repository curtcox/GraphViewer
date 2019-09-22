class RelaxerThread extends Thread {

    private final GraphPanel panel;

    RelaxerThread(GraphPanel panel) {
        this.panel = panel;
    }

    @Override
    @SuppressWarnings("SleepWhileHoldingLock")
    public void run() {
        while (true) {
            panel.advance();
            delay();
        }
    }

    void delay() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {}
    }

}
