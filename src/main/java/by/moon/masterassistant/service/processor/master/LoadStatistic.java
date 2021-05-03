package by.moon.masterassistant.service.processor.master;

import by.moon.masterassistant.bean.BalanceChange;
import by.moon.masterassistant.bean.User;
import by.moon.masterassistant.bean.comparators.BalanceChangeComparatorByDate;
import by.moon.masterassistant.bot.MessageSender;
import by.moon.masterassistant.enums.SystemMessage;
import by.moon.masterassistant.service.beanservice.BalanceChangeService;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoadStatistic {
    private MessageSender messageSender;
    private BalanceChangeService balanceChangeService;

    public void run(User user){
        HSSFWorkbook workbook = new HSSFWorkbook();
        List<BalanceChange> balanceChangeList = balanceChangeService.findAll().stream()
                .sorted(new BalanceChangeComparatorByDate()).collect(Collectors.toList());
        if(balanceChangeList.size() == 0) {
            messageSender.sendMessage(user.getChatId(), SystemMessage.BALANCE_CHANGES_EMPTY.getSystemMessage());
            return;
        }
        LocalDate dateFrom = balanceChangeList.get(0).getDate();
        LocalDate dateTo = balanceChangeList.get(balanceChangeList.size() - 1).getDate();

        for(;!dateFrom.isAfter(dateTo); dateFrom = dateFrom.plusMonths(1)){
            Sheet sheet = workbook.createSheet(dateFrom.getMonth() + "-" + dateFrom.getYear());
            sheet.setColumnWidth(1, 2000);
            sheet.setColumnWidth(2, 2500);
            sheet.setColumnWidth(3, 10000);
            int i = 0;
            for(BalanceChange balanceChange : getByMonth(balanceChangeList, dateFrom)){
                if(i == 0){
                    Row mainRow = sheet.createRow(0);
                    Cell cell0MainRow = mainRow.createCell(0);
                    cell0MainRow.setCellValue("Тип");

                    Cell cell1MainRow = mainRow.createCell(1);
                    cell1MainRow.setCellValue("Сумма");

                    Cell cell2MainRow = mainRow.createCell(2);
                    cell2MainRow.setCellValue("Дата");

                    Cell cell3MainRow = mainRow.createCell(3);
                    cell3MainRow.setCellValue("Комментарий");
                }
                i++;
                Row row = sheet.createRow(i);
                Cell cell0 = row.createCell(0);
                if(balanceChange.isDeposit()) cell0.setCellValue("Приход");
                else cell0.setCellValue("Расход");

                Cell cell1 = row.createCell(1);
                cell1.setCellValue(balanceChange.getAmount());

                Cell cell2 = row.createCell(2);
                cell2.setCellValue(balanceChange.getDate().toString());

                Cell cell3 = row.createCell(3);
                cell3.setCellValue(balanceChange.getComment());
            }
        }

        String filename = "statistic.xlsx";

        try (FileOutputStream out = new FileOutputStream(filename)) {
            workbook.write(out);
            File file = new File(filename);
            messageSender.sendFile(user.getChatId(), file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<BalanceChange> getByMonth(List<BalanceChange> balanceChangeList, LocalDate localDate){
        List<BalanceChange> balanceChangesByDate = new ArrayList<>();
        for(BalanceChange balanceChange : balanceChangeList){
            if(balanceChange.getDate().getMonth() == localDate.getMonth()
            && balanceChange.getDate().getYear() == localDate.getYear()) balanceChangesByDate.add(balanceChange);
        }

        return balanceChangesByDate;
    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Autowired
    public void setBalanceChangeService(BalanceChangeService balanceChangeService) {
        this.balanceChangeService = balanceChangeService;
    }
}
