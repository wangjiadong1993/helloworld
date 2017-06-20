package jiadong.services.downloader;

public enum DownloadStatus {
	/**
	 * No Task At All
	 */
	NONE,
	/**
	 * Task Assigned, waiting for downloading
	 */
	INITIALIZED,
	/**
	 * Task started
	 */
	DOWNLOADING,
	/**
	 * Task paused
	 */
	PAUSED,
	/**
	 * Task stopped due to unexpected error.
	 */
	ERROR,
	/**
	 * Thread terminated by the downloader, due to end of task.
	 */
	TERMINATED
}
