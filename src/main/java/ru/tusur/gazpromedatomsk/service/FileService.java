package ru.tusur.gazpromedatomsk.service;

public interface FileService {

  String createFileExcelForAdmin();

  String createFileExcelForDelivery();

  void sendEmailToAdmin();

  void sendEmailToDelivery();
}
