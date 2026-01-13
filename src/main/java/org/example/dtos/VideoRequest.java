package org.example.dtos;

import jakarta.annotation.sql.DataSourceDefinition;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VideoRequest {
    private String title;
    private String videoUrl;
}
