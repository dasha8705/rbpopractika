package ru.mtuci.antiviruslicensesystem.controller;

import lombok.RequiredArgsConstructor;
import ru.mtuci.antiviruslicensesystem.dto.DeviceDTO;
import ru.mtuci.antiviruslicensesystem.service.DeviceService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/devices")
@RequiredArgsConstructor
public class DeviceController {
    private final DeviceService deviceService;

    @GetMapping
    public ResponseEntity<?> getAllDevices() {
        return ResponseEntity.ok(deviceService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDeviceById(@PathVariable Long id) {
        return ResponseEntity.ok(deviceService.findById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getDevicesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(deviceService.findByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<?> registerDevice(@RequestBody DeviceDTO deviceDTO) {
        return ResponseEntity.ok(deviceService.registerDevice(deviceDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDevice(@PathVariable Long id, @RequestBody DeviceDTO deviceDTO) {
        return ResponseEntity.ok(deviceService.updateDevice(id, deviceDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDevice(@PathVariable Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/count")
    public ResponseEntity<?> getDeviceCount() {
        return ResponseEntity.ok(deviceService.countAll());
    }

    @GetMapping("/{id}/dto")
    public ResponseEntity<?> getDeviceDTO(@PathVariable Long id) {
        return ResponseEntity.ok(deviceService.mapToDTO(deviceService.findById(id)));
    }
}