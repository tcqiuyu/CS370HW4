

public class Buffer {

	private static final int BUFFER_SIZE = 1000;

	private Double[] buffer;
	private int in, out;

	private Producer producer;
	private Consumer consumer;
	private Thread pthread, cthread;

	public Buffer() {
		buffer = new Double[BUFFER_SIZE];
	}

	//link producer and consumer to buffer
	public void link(Producer p, Consumer c) {
		producer = p;
		consumer = c;
	}

	public void produce() {
		synchronized (this) {// not synchronized(producer)
			if ((in + 1) % BUFFER_SIZE == out) {// if buffer is full
				try {
					wait();// not producer.wait()
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {// otherwise
				double newElement = producer.generateElement();
				buffer[in] = newElement;
				in = (in + 1) % BUFFER_SIZE;
				notifyAll();
			}
		}
	}

	public Double consume() {
		Double temp = null;
		synchronized (this) {
			if (in == out) {// if buffer is empty
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				temp = buffer[out];
				buffer[out] = null;
				out = (out + 1) % BUFFER_SIZE;
				notifyAll();
			}
			return temp;
		}
	}

	public void initiate() {

		pthread = new Thread(producer);
		cthread = new Thread(consumer);
		pthread.start();
		cthread.start();
		
		//print message when both threads finish
		try {
			pthread.join();
			cthread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Exiting!");
	}

	public static void main(String[] args) {
		Buffer newBuffer = new Buffer();
		Producer newProducer = new Producer(newBuffer);
		Consumer newConsumer = new Consumer(newBuffer);
		newBuffer.link(newProducer, newConsumer);
		newBuffer.initiate();
	}
}
