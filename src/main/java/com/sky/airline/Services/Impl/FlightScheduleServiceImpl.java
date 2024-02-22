package com.sky.airline.Services.Impl;

import com.sky.airline.Dto.AirportDTO;
import com.sky.airline.Dto.FlightScheduleDTO;
import com.sky.airline.Dto.PlaneDTO;
import com.sky.airline.Entities.Airport;
import com.sky.airline.Entities.FlightSchedule;
import com.sky.airline.Entities.FlightTime;
import com.sky.airline.Entities.Plane;
import com.sky.airline.Repositories.IFlightScheduleRepository;
import com.sky.airline.Services.IFlightScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightScheduleServiceImpl implements IFlightScheduleService {

    private final PlaneServiceImpl planeService;

    private final AirportServiceImpl airportService;

    private final IFlightScheduleRepository flightScheduleRepository;
    private final static String dateTimePattern = "yyyy-MM-dd HH:mm";

    @Override
    public List<FlightSchedule> listSchedule() {
        return flightScheduleRepository.findAll();
    }

    @Override
    public LocalDateTime converToLocalDataTime(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimePattern);
        return LocalDateTime.parse(date, formatter);
    }

    @Override
    public boolean overLimitTime(LocalDateTime timeArrival, LocalDateTime endTimeSchedule) {
        return !timeArrival.isBefore(endTimeSchedule);
    }

    @Override
    public LocalDateTime calculateDate(LocalDateTime currentDate, float EstimateTime) {
        long minutesToAdd = (long) (EstimateTime * 60);
        LocalDateTime resultDateTime = currentDate.plus(minutesToAdd, ChronoUnit.MINUTES);
        return resultDateTime;
    }

    @Override
    public boolean compareTwoDates(LocalDateTime date1, LocalDateTime date2) {
        int diff = date1.compareTo(date2);
        if (diff >= 0) return true;
        return false;
    }

    @Override
    public float getEstimateTime(List<FlightTime> flightTimeDTOList, AirportDTO departureAirport, AirportDTO arrivalAirport) {
        Airport from = airportService.findAirportByAirportName(departureAirport.getAirportName());
        Airport to = airportService.findAirportByAirportName(arrivalAirport.getAirportName());
        List<FlightTime> ls = flightTimeDTOList.stream().filter(i -> i.getFrom().equals(from) && i.getTo().equals(to) || i.getFrom().equals(to) && i.getTo().equals(from)).collect(Collectors.toList());
        return ls.get(0).getEstimateTime();
    }

    @Override
    public List<AirportDTO> listAirportTrim(List<AirportDTO> airportDTOList, AirportDTO currentAirport) {
        List<AirportDTO> listAirportNoCurrentAirport = new ArrayList<>();
        for (AirportDTO airport : airportDTOList) {
            if (!currentAirport.equals(airport)) {
                listAirportNoCurrentAirport.add(airport);
            }
        }
        return listAirportNoCurrentAirport;
    }

    @Override
    public List<PlaneDTO> converToPlaneDTO(List<Plane> planeList, LocalDateTime startDateTime) {
        List<PlaneDTO> planeDTOList = new ArrayList<>();
        for (Plane plane : planeList) {
            if (plane.isOperation()) {
                PlaneDTO p = new PlaneDTO();
                p.setPlaneName(plane.getPlaneName());
                p.setReadyTime(startDateTime);
                planeDTOList.add(p);
            }
        }
        return planeDTOList;
    }

    @Override
    public List<AirportDTO> converToAirportDTO(List<Airport> airportList, LocalDateTime startDateTime) {
        List<AirportDTO> airportDTOList = new ArrayList<>();
        for (Airport airport : airportList) {
            if (airport.isOperation()) {
                AirportDTO a = new AirportDTO();
                List<Plane> planeList = planeService.listAllPlaneOnAirport(airport);
                List<PlaneDTO> planeDTOList = converToPlaneDTO(planeList, startDateTime);
                int i = planeService.currentQuanlityPlaneOnAirport(airport);
                a.setAirportName(airport.getAirportName());
                a.setPlaneLists(planeDTOList);
                a.setQuanlityPlaneCurrent(i);
                airportDTOList.add(a);
            }
        }
        return airportDTOList;
    }

    @Override
    public boolean checkFlightTime(List<FlightTime> flightTimeDTOList, AirportDTO departureAirport, AirportDTO arrivalAirport) {
        Airport from = airportService.findAirportByAirportName(departureAirport.getAirportName());
        Airport to = airportService.findAirportByAirportName(arrivalAirport.getAirportName());
        List<FlightTime> ls = flightTimeDTOList.stream().filter(i -> i.getFrom().equals(from) && i.getTo().equals(to) || i.getFrom().equals(to) && i.getTo().equals(from)).collect(Collectors.toList());
        return !ls.isEmpty();
    }


    @Override
    public List<FlightSchedule> handleFlightSchedule(List<FlightTime> flightTimeList, String start, String end, int action) {
        LocalDateTime startDate = converToLocalDataTime(start);
        LocalDateTime endDate = converToLocalDataTime(end);
        List<FlightScheduleDTO> flightSchedules = new ArrayList<>();
        List<Airport> airportList = airportService.allAirport();
        List<AirportDTO> airportDTOList = converToAirportDTO(airportList, startDate);

        int breakPoint = 0;
        while (breakPoint == 0) {
            LocalDateTime currentTimeFlight = startDate;
            for (int i = 0; i < airportDTOList.toArray().length; i++) {
                List<AirportDTO> listAirportNoCurrentAirport = listAirportTrim(airportDTOList, airportDTOList.get(i));
                int quanlityPlaneCurrent = airportDTOList.get(i).getQuanlityPlaneCurrent();
                Stack<AirportDTO> stackAirport = new Stack<>();
                for (AirportDTO airport : listAirportNoCurrentAirport) {
                    stackAirport.push(airport);
                }
                List<PlaneDTO> listPlaneScheduled = new ArrayList<>();
                for (int j = 0; j < quanlityPlaneCurrent; j++) {
                    if (!stackAirport.isEmpty()) {
                        if (checkFlightTime(flightTimeList, airportDTOList.get(i), stackAirport.peek())) {
                            float estimateTime = getEstimateTime(flightTimeList, airportDTOList.get(i), stackAirport.peek());
                            LocalDateTime readyTime = airportDTOList.get(i).getPlaneLists().get(j).getReadyTime();
                            LocalDateTime timeArrival = calculateDate(readyTime, estimateTime);
                            currentTimeFlight = timeArrival;
                            if (overLimitTime(timeArrival, endDate)) {
                                breakPoint = 1;
                                break;
                            }
                            FlightScheduleDTO flightScheduleDTO = new FlightScheduleDTO();
                            flightScheduleDTO.setFrom(airportDTOList.get(i));
                            stackAirport.peek().getPlaneLists().add(airportDTOList.get(i).getPlaneLists().get(j));
                            stackAirport.peek().setQuanlityPlaneCurrent(stackAirport.peek().getQuanlityPlaneCurrent() + 1);
                            if (action == 1) {
                                Airport airport = airportService.findAirportByAirportName(stackAirport.peek().getAirportName());
                                planeService.updatePlaneOnAirport(airportDTOList.get(i).getPlaneLists().get(j).getPlaneName(), airport);
                            }
                            airportDTOList.get(i).getPlaneLists().get(j).setReadyTime(calculateDate(timeArrival, 1F));
                            flightScheduleDTO.setTo(stackAirport.pop());

                            flightScheduleDTO.setPlaneName(airportDTOList.get(i).getPlaneLists().get(j).getPlaneName());
                            airportDTOList.get(i).setQuanlityPlaneCurrent(airportDTOList.get(i).getQuanlityPlaneCurrent() - 1);
                            listPlaneScheduled.add(airportDTOList.get(i).getPlaneLists().get(j));
                            flightScheduleDTO.setDepartureTime(readyTime);
                            flightScheduleDTO.setArrivalTime(timeArrival);
                            flightSchedules.add(flightScheduleDTO);
                        } else {
                            stackAirport.pop();
                        }
                    }
                }
                for (PlaneDTO planeDTO : listPlaneScheduled) {
                    airportDTOList.get(i).getPlaneLists().remove(planeDTO);
                }
                if (compareTwoDates(currentTimeFlight, endDate)) {
                    breakPoint = 1;
                    break;
                }
            }
            if (breakPoint == 1) break;
        }
        List<FlightSchedule> flightScheduleList = new ArrayList<>();
        int index = 0;
        for (FlightScheduleDTO flightScheduleDTO : flightSchedules) {
            Airport from = airportService.findAirportByAirportName(flightScheduleDTO.getFrom().getAirportName());
            Airport to = airportService.findAirportByAirportName(flightScheduleDTO.getTo().getAirportName());
            FlightSchedule flightSchedule = new FlightSchedule();
            if (action == 0) {
                flightSchedule.setId(index);
            }
            flightSchedule.setDepartureAirport(from);
            flightSchedule.setArrivalAirport(to);
            flightSchedule.setPlaneName(flightScheduleDTO.getPlaneName());
            flightSchedule.setDepartureTime(flightScheduleDTO.getDepartureTime());
            flightSchedule.setArrivalTime(flightScheduleDTO.getArrivalTime());
            flightSchedule.setFlightCode("SKYD" + flightScheduleDTO.getDepartureTime().getDayOfMonth() +
                    flightScheduleDTO.getDepartureTime().getHour() + "H" + flightSchedules.indexOf(flightScheduleDTO));
            flightScheduleList.add(flightSchedule);

            if (action == 1) {
                flightScheduleRepository.save(flightSchedule);
            }
            index++;
        }
        return flightScheduleList;
    }

}