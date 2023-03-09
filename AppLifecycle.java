package io.lifecycle;

import com.azure.data.tables.TableServiceClient;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Deprecated
public class AppLifecycle {
    @Value("${azure.table.datalog.name}")
    private String dataLogTableName;

    @Getter
    private Runnable createAzureTableOnce;

    @Autowired(required = false)
    private TableServiceClient client;

    /**
     * @return
     */
    public Runnable scheduleAzureTableCreation() {
        createAzureTableOnce = () -> {
            Objects.requireNonNull(client, "Azure Table client is not available")
                    .createTableIfNotExists(dataLogTableName);
            createAzureTableOnce = NOP;
        };

        return createAzureTableOnce;
    }

    private final static Runnable NOP = () -> {
        // Do nothing
    };
}
