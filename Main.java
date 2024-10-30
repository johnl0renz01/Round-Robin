import java.io.IOException;
import java.lang.Thread;
import java.lang.Integer;
import java.util.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Main {

	public static final String RED_FG = "\u001B[31m";
	public static final String BLUE_FG = "\u001B[34m";
	public static final String GREEN_FG = "\u001B[32m";
	public static final String COLOR_RESET = "\u001B[0m";

	private static final DecimalFormat df = new DecimalFormat("0.00");

	public static void header() throws IOException, InterruptedException {
		new ProcessBuilder("clear").inheritIO().start().waitFor();
		System.out.println("FL-M5: ACT4 - Round Robin");
		System.out.println("DELA CRUZ, JOHN LORENZ N.\n");
	}

	public static void pressEnterToContinue() {
		System.out.print("\nPress \'Enter\' key to continue...");
		try {
			Scanner scanner = new Scanner(System.in);
			scanner.nextLine();
		} catch (Exception e) {
		}
	}

	public static void processList(int processQuantity, String processVariables[]) {
		System.out.print("Process List: ");
		for (int i = 0; i < processQuantity; i++) {
			if (i == (processQuantity - 1)) {
				System.out.println(processVariables[i]);
			} else {
				System.out.print(processVariables[i] + ", ");
			}
		}
	}

	public static void table(int processQuantity, int processArrivalTime[], String burstTimeText[],
			int sortedTimelineValues[], int timelineLimit, int timelineCounter, String timelineVariables[],
			String processVariables[]) {

		String sortedCompletionTime[] = new String[processQuantity];
		for (int i = 0; i < processQuantity; i++) {
			if (sortedTimelineValues[i] == 0) {
				sortedCompletionTime[i] = " ";
			} else {
				sortedCompletionTime[i] = String.valueOf(sortedTimelineValues[i]);
			}
		}

		Formatter table = new Formatter();
		table.format("\n%1s%2s%1s%2s%1s%2s%1s", "|" + BLUE_FG + "Process" + COLOR_RESET, "|",
				BLUE_FG + "Arrival Time" + COLOR_RESET, "|", GREEN_FG + "      Burst Time      " + COLOR_RESET, "|",
				BLUE_FG + "Completion Time" + COLOR_RESET + "|");

		for (int i = 0; i < processQuantity; i++) {
			table.format("\n%1s", "|");
			table.format("%13s", COLOR_RESET + "P" + (i + 1) + COLOR_RESET);
			table.format("%4s", "|");

			table.format("%15s", COLOR_RESET + processArrivalTime[i] + COLOR_RESET);
			table.format("%7s", "|");
			table.format("%24s", COLOR_RESET + burstTimeText[i] + COLOR_RESET);
			table.format("%8s", "|");

			table.format("%17s", RED_FG + sortedCompletionTime[i] + COLOR_RESET);
			table.format("%8s", "|");
		}
		System.out.println(table);
	}

	public static void timeline(int timelineCounter, int timelineValues[], String timelineVariables[]) {
		System.out.println("\nTimeline:");
		Formatter timeline = new Formatter();

		timeline.format("\n%1s", "| Process | Time Preempted |");
		for (int i = 0; i < timelineCounter; i++) {
			if (timelineVariables[i].equals("idle")) {
				timeline.format("\n%1s", "|   " + timelineVariables[i] + "  |");
			} else {
				timeline.format("\n%1s", "|   " + timelineVariables[i] + "    |");
			}
			timeline.format("%18s%8s", GREEN_FG + timelineValues[i] + COLOR_RESET, "|");
		}
		System.out.println(timeline);
	}

	public static void main(String[] args) throws IOException, InterruptedException {

		header();

		int processQuantity = 0;
		int processCounter = 0;
		int timeSlice = 0;
		boolean flag = false;

		boolean validProcessesQuantity = false;
		boolean validTimeSlice = false;
		System.out.print("Enter no. of Processes: ");

		do {
			try {
				do {
					Scanner input = new Scanner(System.in);
					processQuantity = input.nextInt();
					if (processQuantity <= 0) {
						System.out.println("Invalid input. Please enter a positive number.");
						Thread.sleep(1250);
						header();
						System.out.print("Enter no. of Processes: ");
					}
				} while (processQuantity <= 0);
				validProcessesQuantity = true;
			} catch (InputMismatchException ex) {
				validProcessesQuantity = false;
				System.out.println("Invalid input. Please enter a valid number.");
				Thread.sleep(1250);
				header();
				System.out.print("Enter no. of Processes: ");
			}
		} while (!validProcessesQuantity);

		String processVariables[] = new String[processQuantity];
		for (int i = 0; i < processQuantity; i++) {
			processVariables[i] = "P" + String.valueOf(i + 1);
		}

		String processListVariables[] = new String[processQuantity];
		for (int i = 0; i < processQuantity; i++) {
			processListVariables[i] = "P" + String.valueOf(i + 1);
		}
		int processArrivalTime[] = new int[processQuantity];
		int processBurstTime[] = new int[processQuantity];

		do {
			int arrivalInput = 0;
			int burstInput = 0;
			boolean validArrival = false;
			boolean validBurst = false;
			header();
			System.out.println("Enter no. of Processes: " + processQuantity);
			System.out.println("\nProcess " + (processCounter + 1) + ": ");
			System.out.print("Arrival Time: ");
			do {
				try {
					Scanner input = new Scanner(System.in);
					arrivalInput = input.nextInt();
					if (arrivalInput >= 0) {
						validArrival = true;
					} else {
						System.out.println("Invalid input. Please enter a positive number.");
						Thread.sleep(1250);
						header();
						System.out.println("Enter no. of Processes: " + processQuantity);
						System.out.println("\nProcess " + (processCounter + 1) + ": ");
						System.out.print("Arrival Time: ");
					}
				} catch (InputMismatchException ex) {
					System.out.println("Invalid input. Please enter a valid number.");
					Thread.sleep(1250);
					header();
					System.out.println("Enter no. of Processes: " + processQuantity);
					System.out.println("\nProcess " + (processCounter + 1) + ": ");
					System.out.print("Arrival Time: ");
				}
			} while (!validArrival);

			System.out.print("Burst Time: ");

			do {
				try {
					Scanner input = new Scanner(System.in);
					burstInput = input.nextInt();
					if (burstInput > 0) {
						validBurst = true;
					} else {
						System.out.println("Invalid input. Please enter a positive number.");
						Thread.sleep(1250);
						header();
						System.out.println("Enter no. of Processes: " + processQuantity);
						System.out.println("\nProcess " + (processCounter + 1) + ": ");
						System.out.println("Arrival Time: " + arrivalInput);
						System.out.print("Burst Time: ");
					}
				} catch (InputMismatchException ex) {
					System.out.println("Invalid input. Please enter a valid number.");
					Thread.sleep(1250);
					header();
					System.out.println("Enter no. of Processes: " + processQuantity);
					System.out.println("\nProcess " + (processCounter + 1) + ": ");
					System.out.println("Arrival Time: " + arrivalInput);
					System.out.print("Burst Time: ");
				}
			} while (!validBurst);

			processArrivalTime[processCounter] = arrivalInput;
			processBurstTime[processCounter] = burstInput;
			processCounter++;

		} while (processCounter < processQuantity);

		header();
		System.out.println("Enter no. of Processes: " + processQuantity);
		processList(processQuantity, processVariables);

		do {
			System.out.print("\nEnter time slice: ");
			try {
				do {
					Scanner input = new Scanner(System.in);
					timeSlice = input.nextInt();
					if (timeSlice <= 0) {
						System.out.println("Invalid input. Please enter a positive number.");
						Thread.sleep(1250);
						header();
						System.out.println("Enter no. of Processes: " + processQuantity);
						processList(processQuantity, processVariables);
						System.out.print("\nEnter time slice: ");
					}
				} while (timeSlice <= 0);
				validTimeSlice = true;
			} catch (InputMismatchException ex) {
				validTimeSlice = false;
				System.out.println("Invalid input. Please enter a valid number.");
				Thread.sleep(1250);
				header();
				System.out.println("Enter no. of Processes: " + processQuantity);
				processList(processQuantity, processVariables);
			}
		} while (!validTimeSlice);

		pressEnterToContinue();

		int queueVariablesQuantity = 0;
		int queueVariablesCounter = 0;
		int originalBurstTime[] = new int[processQuantity];

		int sortedTimelineValues[] = new int[processQuantity];
		String burstTimeText[] = new String[processQuantity];
		String substituteTimelineVar[] = new String[queueVariablesQuantity];

		for (int i = 0; i < processQuantity; i++) {
			originalBurstTime[i] = processBurstTime[i];
			burstTimeText[i] = String.valueOf(processBurstTime[i]);
			sortedTimelineValues[i] = 0;
			for (int j = processBurstTime[i]; j > 0; j -= timeSlice) {
				queueVariablesQuantity++;
			}
		}

		header();
		System.out.println("Enter no. of Processes: " + processQuantity);
		processList(processQuantity, processVariables);
		System.out.println("\nEnter time slice: " + timeSlice);
		table(processQuantity, processArrivalTime, burstTimeText, sortedTimelineValues, 0, 0, substituteTimelineVar,
				processVariables);

		int timelineCounter = 0;
		int processIndex = 0;
		int currentArrival = 0;
		int totalArrivalTime = 0;
		int shortestTime = 0;

		// first variable
		for (int i = 0; i < processQuantity; i++) {
			if (i == 0) {
				currentArrival = processArrivalTime[i];
				processIndex = i;
			}

			if (processArrivalTime[i] <= currentArrival) {
				if (processArrivalTime[i] < currentArrival) {
					processIndex = i;
				}
				currentArrival = processArrivalTime[i];

				if (currentArrival == 0) {
					flag = true;
				}

				if (shortestTime == 0) {
					shortestTime = processBurstTime[i];
					processIndex = i;
				} else if (processBurstTime[i] < shortestTime) {
					shortestTime = processBurstTime[i];
					processIndex = i;
				}
			}
		}
		if (!flag) {
			queueVariablesQuantity++;
		}

		int timelineValues[] = new int[queueVariablesQuantity];
		String timelineVariables[] = new String[queueVariablesQuantity];
		String queueVariables[] = new String[queueVariablesQuantity];
		for (int i = 0; i < queueVariablesQuantity; i++) {
			queueVariables[i] = "";
		}

		for (int i = 0; i < queueVariablesQuantity; i++) {
			timelineValues[i] = 0;
			timelineVariables[i] = "";
		}

		int timelineLimit = queueVariablesCounter;

		if (!flag) {
			timelineValues[timelineCounter] = processArrivalTime[processIndex];
			timelineVariables[timelineCounter] = "idle";
			totalArrivalTime = processArrivalTime[processIndex];
			queueVariablesCounter++;
			timelineCounter++;
			pressEnterToContinue();
			// print timeline
			header();
			System.out.println("Enter no. of Processes: " + processQuantity);
			processList(processQuantity, processVariables);
			System.out.println("\nEnter time slice: " + timeSlice);
			table(processQuantity, processArrivalTime, burstTimeText, sortedTimelineValues, timelineLimit,
					timelineCounter, timelineVariables, processVariables);
			timeline(timelineCounter, timelineValues, timelineVariables);
		} else {
			queueVariablesCounter++;
		}

		boolean processChanged = false;

		for (int counter = 0; counter < queueVariablesQuantity; counter++) {
			int lastProcessIndex = 0;
			int removedIndex = 0;
			boolean stop = false;
			boolean repeat = true;
			boolean cycle = true;
			flag = false;

			while (repeat) {
				for (int i = 0; i < processQuantity; i++) {
					boolean processLocated = false;
					// Deduction process
					if (cycle) {
						for (int j = 0; j < queueVariablesQuantity; j++) {
							for (int k = 0; k < processQuantity; k++) {
								if (queueVariables[j].equals(processVariables[k])) {
									processLocated = true;
									processIndex = k;
									removedIndex = k;
									queueVariables[j] = "DONE";
									break;
								}
							}
							if (processLocated) {
								break;
							}
						}

						for (int j = 0; j < processQuantity; j++) {
							if (processVariables[processIndex].equals(processVariables[j])) {
								if (processBurstTime[j] > timeSlice) {
									processBurstTime[j] -= timeSlice;
									totalArrivalTime += timeSlice;
								} else {
									totalArrivalTime += processBurstTime[j];
									processBurstTime[j] -= processBurstTime[j];
								}

								timelineValues[timelineCounter] = totalArrivalTime;
								timelineVariables[timelineCounter] = processVariables[processIndex];
								burstTimeText[j] = burstTimeText[j] + "/" + String.valueOf(processBurstTime[j]);

								timelineCounter++;
								processChanged = true;
								cycle = false;
								repeat = false;
								break;
							}
						}
					}

					// skip if variable is in current queue
					boolean skip = false;
					for (int j = 0; j < queueVariablesQuantity; j++) {
						if (processVariables[i].equals(queueVariables[j])) {
							skip = true;
							break;
						}
					}

					if (skip) {
						if (i == (processQuantity - 1)) {
							if (removedIndex != 0 && totalArrivalTime >= processArrivalTime[removedIndex]
									&& processBurstTime[removedIndex] != 0) {
								queueVariables[queueVariablesCounter] = processVariables[removedIndex];
								queueVariablesCounter++;
							}
						}
						continue;
					}

					// check remaining processes
					if (i != removedIndex && processVariables[i] != "P1" && processBurstTime[i] != 0) {
						if (totalArrivalTime > processArrivalTime[i]) {
							queueVariables[queueVariablesCounter] = processVariables[i];
							queueVariablesCounter++;
						} else if (totalArrivalTime == processArrivalTime[i]) {
							lastProcessIndex = i;
						}
					}

					// check first process
					if (i == (processQuantity - 1)) {
						for (int j = 0; j < queueVariablesQuantity; j++) {
							if (processVariables[0].equals(queueVariables[j])) {
								skip = true;
								break;
							}
						}

						if (!skip) {
							if (totalArrivalTime >= processArrivalTime[0] && processBurstTime[0] != 0) {
								queueVariables[queueVariablesCounter] = processVariables[0];
								queueVariablesCounter++;
							}
						}

						if (removedIndex != 0 && totalArrivalTime >= processArrivalTime[removedIndex]
								&& processBurstTime[removedIndex] != 0) {
							queueVariables[queueVariablesCounter] = processVariables[removedIndex];
							queueVariablesCounter++;
						}

						if (lastProcessIndex != 0) {
							queueVariables[queueVariablesCounter] = processVariables[lastProcessIndex];
							queueVariablesCounter++;
						}
					}
				}

				if (processChanged) {
					pressEnterToContinue();
					// print timeline
					header();
					System.out.println("Enter no. of Processes: " + processQuantity);
					processList(processQuantity, processVariables);
					System.out.println("\nEnter time slice: " + timeSlice);
					table(processQuantity, processArrivalTime, burstTimeText, sortedTimelineValues, timelineLimit,
							timelineCounter, timelineVariables, processVariables);
					timeline(timelineCounter, timelineValues, timelineVariables);
					processChanged = false;

					if (timelineCounter == (queueVariablesQuantity)) {
						stop = true;
					}

					if (!repeat) {
						break;
					}
				}
			}
			if (stop) {
				break;
			}
		}

		pressEnterToContinue();
		// OUTSIDE
		for (int i = 0; i < processQuantity; i++) {
			for (int j = ((queueVariablesQuantity) - 1); j >= 0; j--) {
				if (processVariables[i].equals(timelineVariables[j])) {
					sortedTimelineValues[i] = timelineValues[j];
					break;
				}
			}
		}

		int valuesTAT[] = new int[processQuantity];
		int valuesWT[] = new int[processQuantity];

		for (int i = 0; i < processQuantity; i++) {
			valuesTAT[i] = sortedTimelineValues[i] - processArrivalTime[i];
			valuesWT[i] = valuesTAT[i] - originalBurstTime[i];
		}

		// computing average TAT & WT
		float averageTAT = 0;
		float averageWT = 0;

		for (int i = 0; i < processQuantity; i++) {
			averageTAT += valuesTAT[i];
			averageWT += valuesWT[i];
		}

		averageTAT /= processQuantity;
		averageWT /= processQuantity;

		header();
		System.out.println("Enter no. of Processes: " + processQuantity);
		processList(processQuantity, processVariables);
		System.out.println("\nEnter time slice: " + timeSlice);
		table(processQuantity, processArrivalTime, burstTimeText, sortedTimelineValues, timelineLimit, timelineCounter,
				timelineVariables, processVariables);
		timeline(timelineCounter, timelineValues, timelineVariables);

		System.out.println("\nAverage Turnaround Time: " + RED_FG + df.format(averageTAT) + COLOR_RESET);
		System.out.println("Average Waiting Time: " + RED_FG + df.format(averageWT) + COLOR_RESET);
	}
}