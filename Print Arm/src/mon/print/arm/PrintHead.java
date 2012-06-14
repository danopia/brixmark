package mon.print.arm;

public class PrintHead extends StateController {
	PrintHead() {
		super("Mon Print Head");
	}

	void gripOn()  { setState(1, 1); }
	void gripOff() { setState(1, 0); }

	void pressOn()  { setState(0, 1); }
	void pressOff() { setState(0, 0); }
}
