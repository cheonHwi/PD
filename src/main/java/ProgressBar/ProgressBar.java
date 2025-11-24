package ProgressBar;

public class ProgressBar {
    private final int total;
    private int current;
    private final int barLength;
    private final long startTime;

    public ProgressBar(int total) {
        this(total, 50);
    }

    public ProgressBar(int total, int barLength) {
        this.total = total;
        this.current = 0;
        this.barLength = barLength;
        this.startTime = System.currentTimeMillis();
    }

    public void update(int current) {
        this.current = current;
        display();
    }

    public void increment() {
        this.current++;
        display();
    }

    public void finish() {
        this.current = this.total;
        display();
        System.out.println(); // 줄바꿈
    }

    private void display() {
        int percentage = (int) ((current * 100.0) / total);
        int filled = (int) ((current * barLength) / total);

        StringBuilder bar = new StringBuilder();
        bar.append("\r"); // 커서를 줄 처음으로
        bar.append("[");

        bar.append("=".repeat(Math.max(0, filled)));

        if (filled < barLength) {
            bar.append(">");
        }

        bar.append(" ".repeat(Math.max(0, barLength - (filled + 1))));

        bar.append("] ");
        bar.append(String.format("%3d%%", percentage));
        bar.append(String.format(" (%d/%d)", current, total));

        // 경과 시간
        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        if (elapsed > 0) {
            bar.append(String.format(" - %ds", elapsed));
        }

        System.out.print(bar);
        System.out.flush();
    }
}