package ma.streaming.upload.shared.runner;

import lombok.RequiredArgsConstructor;
import ma.streaming.upload.actor.ActorEntity;
import ma.streaming.upload.actor.ActorJpaRepository;
import ma.streaming.upload.genre.GenreEntity;
import ma.streaming.upload.genre.GenreJpaRepository;
import ma.streaming.upload.role.RoleEntity;
import ma.streaming.upload.role.RoleJpaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommandRunner implements CommandLineRunner {
    private static final List<ActorEntity> actors;
    private static final List<GenreEntity> genres;
    private static final List<RoleEntity> roles;

    private final ActorJpaRepository actorJpaRepository;
    private final GenreJpaRepository genreJpaRepository;
    private final RoleJpaRepository roleJpaRepository;

    static {
        actors = List.of(
                ActorEntity.builder().name("Robert Downey Jr").build(),
                ActorEntity.builder().name("Scarlett Johansson").build(),
                ActorEntity.builder().name("Leonardo DiCaprio").build(),
                ActorEntity.builder().name("Meryl Streep").build(),
                ActorEntity.builder().name("Tom Hanks").build(),
                ActorEntity.builder().name("Denzel Washington").build(),
                ActorEntity.builder().name("Brad Pitt").build(),
                ActorEntity.builder().name("Angelina Jolie").build(),
                ActorEntity.builder().name("Johnny Depp").build(),
                ActorEntity.builder().name("Natalie Portman").build(),
                ActorEntity.builder().name("Matt Damon").build(),
                ActorEntity.builder().name("Emma Stone").build(),
                ActorEntity.builder().name("Jennifer Lawrence").build(),
                ActorEntity.builder().name("Chris Hemsworth").build(),
                ActorEntity.builder().name("Chris Evans").build(),
                ActorEntity.builder().name("Morgan Freeman").build(),
                ActorEntity.builder().name("Julia Roberts").build(),
                ActorEntity.builder().name("Anne Hathaway").build(),
                ActorEntity.builder().name("Christian Bale").build(),
                ActorEntity.builder().name("Hugh Jackman").build(),
                ActorEntity.builder().name("Cate Blanchett").build(),
                ActorEntity.builder().name("Ryan Gosling").build(),
                ActorEntity.builder().name("Charlize Theron").build(),
                ActorEntity.builder().name("Keanu Reeves").build(),
                ActorEntity.builder().name("Gal Gadot").build(),
                ActorEntity.builder().name("Benedict Cumberbatch").build(),
                ActorEntity.builder().name("Joaquin Phoenix").build(),
                ActorEntity.builder().name("Sandra Bullock").build(),
                ActorEntity.builder().name("Tom Cruise").build(),
                ActorEntity.builder().name("Will Smith").build(),
                ActorEntity.builder().name("Harrison Ford").build(),
                ActorEntity.builder().name("Nicole Kidman").build(),
                ActorEntity.builder().name("Amy Adams").build(),
                ActorEntity.builder().name("Jake Gyllenhaal").build(),
                ActorEntity.builder().name("Mark Ruffalo").build(),
                ActorEntity.builder().name("Zendaya").build(),
                ActorEntity.builder().name("Timothée Chalamet").build(),
                ActorEntity.builder().name("Florence Pugh").build(),
                ActorEntity.builder().name("Viola Davis").build(),
                ActorEntity.builder().name("Margot Robbie").build(),
                ActorEntity.builder().name("Daniel Radcliffe").build(),
                ActorEntity.builder().name("Emma Watson").build(),
                ActorEntity.builder().name("Rupert Grint").build(),
                ActorEntity.builder().name("Eddie Redmayne").build(),
                ActorEntity.builder().name("Millie Bobby Brown").build(),
                ActorEntity.builder().name("Saoirse Ronan").build(),
                ActorEntity.builder().name("Jared Leto").build(),
                ActorEntity.builder().name("Chris Pratt").build(),
                ActorEntity.builder().name("Jason Momoa").build(),
                ActorEntity.builder().name("Jessica Chastain").build(),
                ActorEntity.builder().name("Kristen Wiig").build(),
                ActorEntity.builder().name("Jamie Foxx").build(),
                ActorEntity.builder().name("Christoph Waltz").build(),
                ActorEntity.builder().name("Tim Robbins").build(),
                ActorEntity.builder().name("Bob Gunton").build(),
                ActorEntity.builder().name("John Travolta").build(),
                ActorEntity.builder().name("Uma Thurman").build(),
                ActorEntity.builder().name("Samuel L. Jackson").build(),
                ActorEntity.builder().name("Joseph Gordon-Levitt").build(),
                ActorEntity.builder().name("Elliot Page").build(),
                ActorEntity.builder().name("Matthew McConaughey").build(),
                ActorEntity.builder().name("Robin Williams").build(),
                ActorEntity.builder().name("Heath Ledger").build(),
                ActorEntity.builder().name("Shailene Woodley").build(),
                ActorEntity.builder().name("Tom Hardy").build(),
                ActorEntity.builder().name("Zoe Saldana").build(),
                ActorEntity.builder().name("Ben Affleck").build(),
                ActorEntity.builder().name("Rosamund Pike").build(),
                ActorEntity.builder().name("Michael Fassbender").build(),
                ActorEntity.builder().name("Rachel McAdams").build(),
                ActorEntity.builder().name("Ewan McGregor").build(),
                ActorEntity.builder().name("Lupita Nyong'o").build(),
                ActorEntity.builder().name("Idris Elba").build(),
                ActorEntity.builder().name("Gillian Anderson").build(),
                ActorEntity.builder().name("Bryan Cranston").build(),
                ActorEntity.builder().name("Elizabeth Olsen").build(),
                ActorEntity.builder().name("Paul Rudd").build(),
                ActorEntity.builder().name("Chadwick Boseman").build(),
                ActorEntity.builder().name("Michael B. Jordan").build(),
                ActorEntity.builder().name("Andrew Garfield").build(),
                ActorEntity.builder().name("Tobey Maguire").build(),
                ActorEntity.builder().name("Kirsten Dunst").build(),
                ActorEntity.builder().name("J.K. Simmons").build(),
                ActorEntity.builder().name("Octavia Spencer").build(),
                ActorEntity.builder().name("Melissa McCarthy").build(),
                ActorEntity.builder().name("Reese Witherspoon").build(),
                ActorEntity.builder().name("Penélope Cruz").build(),
                ActorEntity.builder().name("Javier Bardem").build(),
                ActorEntity.builder().name("Steve Carell").build(),
                ActorEntity.builder().name("Jonah Hill").build(),
                ActorEntity.builder().name("Jeff Bridges").build(),
                ActorEntity.builder().name("Jeremy Renner").build(),
                ActorEntity.builder().name("Tilda Swinton").build(),
                ActorEntity.builder().name("Naomi Watts").build(),
                ActorEntity.builder().name("Halle Berry").build(),
                ActorEntity.builder().name("Kate Winslet").build(),
                ActorEntity.builder().name("Helen Mirren").build(),
                ActorEntity.builder().name("Maggie Smith").build()
        );


        genres = List.of(
                GenreEntity.builder().name("Action").build(),
                GenreEntity.builder().name("Adventure").build(),
                GenreEntity.builder().name("Animation").build(),
                GenreEntity.builder().name("Biography").build(),
                GenreEntity.builder().name("Comedy").build(),
                GenreEntity.builder().name("Crime").build(),
                GenreEntity.builder().name("Documentary").build(),
                GenreEntity.builder().name("Drama").build(),
                GenreEntity.builder().name("Family").build(),
                GenreEntity.builder().name("Fantasy").build(),
                GenreEntity.builder().name("Film-Noir").build(),
                GenreEntity.builder().name("History").build(),
                GenreEntity.builder().name("Horror").build(),
                GenreEntity.builder().name("Music").build(),
                GenreEntity.builder().name("Musical").build(),
                GenreEntity.builder().name("Mystery").build(),
                GenreEntity.builder().name("Romance").build(),
                GenreEntity.builder().name("Sci-Fi").build(),
                GenreEntity.builder().name("Sport").build(),
                GenreEntity.builder().name("Thriller").build(),
                GenreEntity.builder().name("War").build(),
                GenreEntity.builder().name("Western").build()
        );

        roles = List.of(
                RoleEntity.builder().name("ROLE_USER").description("Standard user with limited access to content and viewing").build(),
                RoleEntity.builder().name("ROLE_PREMIUM_USER").description("Premium user with access to exclusive content and features").build(),
                RoleEntity.builder().name("ROLE_ADMIN").description("Administrator with full access to manage the platform, users, and content").build(),
                RoleEntity.builder().name("ROLE_MANAGER").description("Manager responsible for overseeing content and user interactions").build(),
                RoleEntity.builder().name("ROLE_MODERATOR").description("Moderator with the ability to manage and moderate user-generated content").build(),
                RoleEntity.builder().name("ROLE_CONTENT_CREATOR").description("User who can upload, manage, and create new content on the platform").build()
        );
    }

    @Override
    public void run(String... args) {
        actors.forEach(actor -> {
            if (!this.actorJpaRepository.existsByNameIgnoreCase(actor.getName()))
                this.actorJpaRepository.saveAndFlush(actor);
        });

        genres.forEach(genre -> {
            if (!this.genreJpaRepository.existsByNameIgnoreCase(genre.getName()))
                this.genreJpaRepository.saveAndFlush(genre);
        });

        roles.forEach(role -> {
            if (!this.roleJpaRepository.existsByNameIgnoreCase(role.getName()))
                this.roleJpaRepository.saveAndFlush(role);
        });
    }
}
