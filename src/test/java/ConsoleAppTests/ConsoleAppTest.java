package ConsoleAppTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.stream.controller.UserController;
import org.stream.model.*;
import org.stream.model.exceptions.EntityNotFoundException;
import org.stream.service.ContentService;
import org.stream.service.SessionService;
import org.stream.service.UserService;
import org.stream.presentation.ConsoleApp;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConsoleAppTests {

    private ConsoleApp app;
    private UserController userController;
    private ContentService contentService;
    private SessionService sessionService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        contentService = mock(ContentService.class);
        sessionService = mock(SessionService.class);
        userService = mock(UserService.class);
        userController = new UserController(userService, contentService, sessionService);
        app = new ConsoleApp();
    }

    @Test
    void testAddMovie_Success() {
        Movie movie = new Movie(UUID.randomUUID(), "Inception", 120, 8.5);
        doNothing().when(contentService).addMovie(movie);
        userController.getContentService().addMovie(movie);
        verify(contentService, times(1)).addMovie(movie);
    }

    @Test
    void testAddMovie_Failure() {
        Movie movie = new Movie(UUID.randomUUID(), "", 120, 8.5);
        doThrow(new RuntimeException("Invalid movie title")).when(contentService).addMovie(movie);
        Exception exception = assertThrows(RuntimeException.class, () ->
                userController.getContentService().addMovie(movie));
        assertEquals("Invalid movie title", exception.getMessage());
    }

    @Test
    void testAddSerial_Success() {
        Serial serial = new Serial(UUID.randomUUID(), "Breaking Bad", List.of(), 9.5);
        doNothing().when(contentService).addSerial(serial);
        userController.getContentService().addSerial(serial);
        verify(contentService, times(1)).addSerial(serial);
    }

    @Test
    void testAddSerial_Failure() {
        Serial serial = new Serial(UUID.randomUUID(), "", List.of(), 9.5);
        doThrow(new RuntimeException("Invalid serial title")).when(contentService).addSerial(serial);
        Exception exception = assertThrows(RuntimeException.class, () ->
                userController.getContentService().addSerial(serial));
        assertEquals("Invalid serial title", exception.getMessage());
    }

    @Test
    void testAddUser_Success() {
        User user = new User("john_doe", "password123", new FreeAccount(), false);
        doNothing().when(userService).registerUser(user);
        userController.registerUser(user);
        verify(userService, times(1)).registerUser(user);
    }

    @Test
    void testAddUser_Failure() {
        User user = new User("", "password123", new FreeAccount(), false);
        doThrow(new RuntimeException("Invalid username")).when(userService).registerUser(user);
        Exception exception = assertThrows(RuntimeException.class, () ->
                userController.registerUser(user));
        assertEquals("Invalid username", exception.getMessage());
    }

    @Test
    void testLogin_Success() {
        when(sessionService.isUserLoggedIn()).thenReturn(false);
        when(userService.authenticateUser("john_doe", "password123")).thenReturn(Optional.of(new User(UUID.randomUUID(), "john_doe", "password123", false)));
        boolean result = userController.login("john_doe", "password123");
        assertTrue(result);
    }

    @Test
    void testLogin_Failure() {
        when(userService.authenticateUser("wrong_user", "wrong_pass")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () ->
                userController.login("wrong_user", "wrong_pass")
        );
    }


    @Test
    void testDeleteMovie_Success() {
        String movieTitle = "Inception";
        doNothing().when(contentService).removeMovie(movieTitle);
        userController.getContentService().removeMovie(movieTitle);
        verify(contentService, times(1)).removeMovie(movieTitle);
    }

    @Test
    void testDeleteMovie_Failure() {
        String movieTitle = "NonExistingMovie";
        doThrow(new RuntimeException("Movie not found")).when(contentService).removeMovie(movieTitle);
        Exception exception = assertThrows(RuntimeException.class, () ->
                userController.getContentService().removeMovie(movieTitle));
        assertEquals("Movie not found", exception.getMessage());
    }

    @Test
    void testDeleteSerial_Success() {
        String serialTitle = "Breaking Bad";
        doNothing().when(contentService).removeSerial(serialTitle);
        userController.getContentService().removeSerial(serialTitle);
        verify(contentService, times(1)).removeSerial(serialTitle);
    }

    @Test
    void testDeleteSerial_Failure() {
        String serialTitle = "NonExistingSerial";
        doThrow(new RuntimeException("Serial not found")).when(contentService).removeSerial(serialTitle);
        Exception exception = assertThrows(RuntimeException.class, () ->
                userController.getContentService().removeSerial(serialTitle));
        assertEquals("Serial not found", exception.getMessage());
    }

    @Test
    void testViewTop10Movies_Success() {
        List<Movie> topMovies = List.of(new Movie(UUID.randomUUID(), "Movie1", 120, 8.9));
        when(contentService.getTop10Movies()).thenReturn(topMovies);
        List<Movie> result = userController.getContentService().getTop10Movies();
        assertEquals(topMovies, result);
    }

    @Test
    void testViewTop10Movies_Failure() {
        when(contentService.getTop10Movies()).thenThrow(new RuntimeException("No movies available"));
        Exception exception = assertThrows(RuntimeException.class, () ->
                userController.getContentService().getTop10Movies());
        assertEquals("No movies available", exception.getMessage());
    }

    @Test
    void testViewTop10Serials_Success() {
        List<Serial> topSerials = List.of(new Serial(UUID.randomUUID(), "Breaking Bad", List.of(), 9.5));
        when(contentService.getTop10Serials()).thenReturn(topSerials);
        List<Serial> result = userController.getContentService().getTop10Serials();
        assertEquals(topSerials, result);
    }

    @Test
    void testViewTop10Serials_Failure() {
        when(contentService.getTop10Serials()).thenThrow(new RuntimeException("No serials available"));
        Exception exception = assertThrows(RuntimeException.class, () ->
                userController.getContentService().getTop10Serials());
        assertEquals("No serials available", exception.getMessage());
    }


    @Test
    void testViewMovies_Success() {
        List<Movie> movies = List.of(new Movie(UUID.randomUUID(), "Movie1", 120, 8.5));
        when(contentService.getAllMovies()).thenReturn(movies);
        List<Movie> result = userController.getContentService().getAllMovies();
        assertEquals(movies, result);
    }

    @Test
    void testViewMovies_Failure() {
        when(contentService.getAllMovies()).thenThrow(new RuntimeException("No movies available"));
        Exception exception = assertThrows(RuntimeException.class, () ->
                userController.getContentService().getAllMovies());
        assertEquals("No movies available", exception.getMessage());
    }

    @Test
    void testDeleteUser_Success() {
        String username = "john_doe";
        doNothing().when(userService).deleteUser(username);
        userController.getUserService().deleteUser(username);
        verify(userService, times(1)).deleteUser(username);
    }

    @Test
    void testDeleteUser_Failure() {
        String username = "unknown_user";
        doThrow(new RuntimeException("User not found")).when(userService).deleteUser(username);
        Exception exception = assertThrows(RuntimeException.class, () ->
                userController.getUserService().deleteUser(username));
        assertEquals("User not found", exception.getMessage());
    }
}
