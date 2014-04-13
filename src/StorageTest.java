import org.junit.Test;
import static org.junit.Assert.*;

//@author A0097706U
public class StorageTest {
	Storage testStorage = Storage.getInstance();

	@Test
	public void validTest() {
		//Valid Samples
		String validSetDuration 		= "150414|1000      |1100    |Task                                                        |SOC                           |setDuration |done";
		String validFullDay 			= "160414|-         |-       |Task                                                        |SOC                           |fullDay     |done";
		String validNoSetTiming 		= "-     |-         |-       |Task                                                        |SOC                           |noSetTiming |done";
		String validTargetTime 		= "170414|1200      |-       |Task                                                        |SOC                           |targetedTime|done";
		Task validSetDurationTask 	= new Task("setDuration", "task", "150414", "1000", "1100", "SOC", true);
		Task validFullDayTask 		= new Task("fullDay", "task", "160414", null, null, "SOC", true);
		Task validNoSetTimingTask		= new Task("noSetTiming", "task", null, null, null, "SOC", true);
		Task validTargetedTimeTask 	= new Task("targetedTime", "task", "170414", "1200", null, "SOC", true);

		//Correct Tests
		testConvertStringToTaskTrue(validSetDuration, validSetDurationTask);
		testConvertStringToTaskTrue(validFullDay, validFullDayTask);
		testConvertStringToTaskTrue(validNoSetTiming, validNoSetTimingTask);
		testConvertStringToTaskTrue(validTargetTime, validTargetedTimeTask);

		testConvertTaskToStringTrue(validSetDuration, validSetDurationTask);
		testConvertTaskToStringTrue(validFullDay, validFullDayTask);
		testConvertTaskToStringTrue(validNoSetTiming, validNoSetTimingTask);
		testConvertTaskToStringTrue(validTargetTime, validTargetedTimeTask);

		testWriteATaskToFileTrue(validSetDurationTask);
		testWriteATaskToFileTrue(validFullDayTask);
		testWriteATaskToFileTrue(validNoSetTimingTask);
		testWriteATaskToFileTrue(validTargetedTimeTask);
	}
	
	@Test
	public void invalidTest() {
		//Wrong Samples
		String wrongSetDuration 	= "150414|1s00      |1100    |Task                                                        |SOC                           |setDuration |done";
		String wrongFullDay 		= "1604d4|a         |-       |Task                                                        |SOC                           |fullDay     |done";
		String wrongNoSetTiming 	= "-     |-         |-       |Task                                                        |SOC                           |noSetTiming |donde";
		String wrongTargetTime 		= "abc   |1rr0      |-       |Task                                                        |SOC                           |targetedTime|done";
		Task wrongSetDurationTask 	= new Task("setDuration", "task", "150414", "1s00", "1100", "SOC", true);
		Task wrongFullDayTask 		= new Task("fullDay", "task", "1604d4", null, null, "*OC", true);
		Task wrongNoSetTimingTask	= new Task("noSetTiming", "task", "324", null, null, "SOC", true);
		Task wrongTargetedTimeTask 	= new Task("targetedTime", "task", "abc", "1rr0", null, "#OC", true);

		//Wrong Tests
		testConvertStringToTaskFalse(wrongSetDuration);
		testConvertStringToTaskFalse(wrongFullDay);
		testConvertStringToTaskFalse(wrongNoSetTiming);
		testConvertStringToTaskFalse(wrongTargetTime);

		testConvertTaskToStringFalse(wrongSetDurationTask);
		testConvertTaskToStringFalse(wrongFullDayTask);
		testConvertTaskToStringFalse(wrongNoSetTimingTask);
		testConvertTaskToStringFalse(wrongTargetedTimeTask);

		testWriteATaskToFileFalse(wrongSetDurationTask);
		testWriteATaskToFileFalse(wrongFullDayTask);
		testWriteATaskToFileFalse(wrongNoSetTimingTask);
		testWriteATaskToFileFalse(wrongTargetedTimeTask);
	}
	
	@Test
	public void invalidCombinationTest() {
		//Wrong Combination Samples
		String wrongDate 			= "380414|1000      |1100    |Task                                                        |SOC                           |setDuration |done";
		String wrongLocationName	= "160414|-         |-       |Task                                                        |#OC                           |fullDay     |done";
		String wrongTimeDuration 	= "170414|1200      |1100    |Task                                                        |SOC                           |setDuration |undone";
		Task wrongDateTask		 	= new Task("setDuration", "task", "380414", "1000", "1100", "SOC", true);
		Task wrongLocationTask		= new Task("targetedTime", "task", "160414", null, null, "#OC", true);
		Task wrongTimeDurationTask	= new Task("setDuration", "task", "170414", "1200", "1100", "SOC", false);

		//Wrong Combination Tests
		testConvertStringToTaskFalse(wrongDate);
		testConvertStringToTaskFalse(wrongLocationName);
		testConvertStringToTaskFalse(wrongTimeDuration);

		testConvertTaskToStringFalse(wrongDateTask);
		testConvertTaskToStringFalse(wrongLocationTask);
		testConvertTaskToStringFalse(wrongTimeDurationTask);

		testWriteATaskToFileFalse(wrongDateTask);
		testWriteATaskToFileFalse(wrongLocationTask);
		testWriteATaskToFileFalse(wrongTimeDurationTask);
	}
	
	@Test
	public void otherTest() {
		//Other Tests
		Task validSetDurationTask 	= new Task("setDuration", "task", "150414", "1000", "1100", "SOC", true);
		
		testSetTaskListTrue(validSetDurationTask);
		testSetTaskListFalse(null);
		testClear();
	}

	//Test: Convert valid String to Task
	private void testConvertStringToTaskTrue(String splitedSample, Task validSampleTask) {		
		System.out.println("Converting String to Task - True");
		Task sampleTask;
		sampleTask = testStorage.convertStringToTask(splitedSample.split("\\|"));

		assertEquals(validSampleTask.getName(), sampleTask.getName());
		assertEquals(validSampleTask.getDate(), sampleTask.getDate());
		assertEquals(validSampleTask.getStartTime(), sampleTask.getStartTime());
		assertEquals(validSampleTask.getEndTime(), sampleTask.getEndTime());
		assertEquals(validSampleTask.getLocation(), sampleTask.getLocation());
		assertEquals(validSampleTask.getType(), sampleTask.getType());
		assertEquals(validSampleTask.getIsDone(), sampleTask.getIsDone());
	}

	//Test: Convert invalid String to Task
	private void testConvertStringToTaskFalse(String splitedSample) {		
		System.out.println("Converting String to Task - False");
		Task sampleTask2 = testStorage.convertStringToTask(splitedSample.split("\\|"));

		assertEquals(null, sampleTask2);
	}

	//Test: Convert valid Task to String
	private void testConvertTaskToStringTrue(String sampleString, Task sampleTask) {
		System.out.println("Converting Task to String - True");
		assertEquals(sampleString, testStorage.convertTaskToString(sampleTask));
	}

	//Test: Convert invalid Task to String
	private void testConvertTaskToStringFalse(Task wrongSampleTask) {
		System.out.println("Converting Task to String - False");
		assertEquals(false, testStorage.writeATaskToFile(wrongSampleTask, false));
	}

	//Test: Convert valid Task to File
	private void testWriteATaskToFileTrue(Task validSampleTask) {
		System.out.println("Writing Task to File - True");
		assertEquals(true, testStorage.writeATaskToFile(validSampleTask, false));
	}

	//Test: Convert invalid Task to File
	private void testWriteATaskToFileFalse(Task wrongSampleTask) {
		System.out.println("Writing Task to File - False");
		assertEquals(false, testStorage.writeATaskToFile(wrongSampleTask, false));
	}
	
	//Test: Add task to taskList
	private void testSetTaskListTrue(Task sampleTask) {
		System.out.println("Setting Task List - True");
		assertEquals(true, testStorage.setTaskList(sampleTask));
	}

	//Test: Add null task to taskList
	private void testSetTaskListFalse(Task sampleTask) {
		System.out.println("Setting Task List - False");
		assertEquals(false, testStorage.setTaskList(sampleTask));
	}

	//Test: Clear file
	private void testClear() {
		System.out.println("Clear File");
		assertEquals(true, testStorage.clear());
	}
}