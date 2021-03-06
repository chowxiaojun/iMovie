package com.xiroid.imovie.model;

import java.util.List;

/**
 * @author xiaojunzhou
 * @date 16/6/14
 */
public class Reviews {
    /**
     * id : 100402
     * page : 1
     * results : [{"id":"535868070e0a26067f0007d2","author":"Raquel98","content":"This movie is very good I loved it so much.","url":"https://www.themoviedb.org/review/535868070e0a26067f0007d2"},{"id":"53f4879b0e0a267f8d001da8","author":"Per Gunnar Jonsson","content":"This is indeed a great piece of superhero entertainment. If superheroes and action, plenty of action, is within your sphere of interest then this movie truly delivers. There is even a story to support the stunts and special effects fireworks that is not too shabby. As a matter of fact, for being a Marvel-based movie, the story is more than acceptable.\r\n\r\nAs the blurb states, our hero is meeting a few adaptation issues after having been awaken into the modern world. Luckily the movie do not fall into the trap of making some silly comedy out of it but keeps these parts at a reasonable level where it stays funny without ruining the movie or disrupting the pace of the real story.\r\n\r\nThe story? Simple and not very original with the usual slew of traitors strewn about, you cannot trust anyone, our hero becomes a fugitive etc etc bla bla bla. It is however a pretty straightforward one which works quite well and is not getting bogged down in silly emotions and general idiotic behavior by our hero like for instance several instalments of Spider Man have done. Actually this is one thing that I really like with this movie. The hero is a true hero from start to finish without any of that modern Hollywood crap where the hero must be tarnished in one way or another to satisfy the directors and producers.\r\n\r\nAs I wrote above if you like superhero action movies then there is little not to like with this one. Acceptable story, plenty of action, good special effects, a cool chick (hey I am but an ordinary male, I like female actors in tight costumes) and a clear hero. None of the actors are really doing a bad job of it and in general the acting is as good as one can expect when portraying characters that, after all, are be totally ludicrous in real life. Its 8 out of 10 rating at IMDb (at the time of writing this) is well deserved. I am astonished that some people seem to have voted it down with a one star rating. It is a Marvel-based movie. You might feel it is more or less good but the style of the movie should not really be a major surprise to anyone and there is no way in hell that it deserves a one star rating unless someone really watched the wrong movie by accident.","url":"https://www.themoviedb.org/review/53f4879b0e0a267f8d001da8"},{"id":"544e11070e0a2608d4004175","author":"Andres Gomez","content":"Put *a little* bit of brain in a superhero's movie script and you will have everybody talking wonders about it.\r\n\r\nThis is not a bad movie but neither astonishing. Fun and entertaining and with great FX, as expected.","url":"https://www.themoviedb.org/review/544e11070e0a2608d4004175"},{"id":"54590aff0e0a261165002061","author":"Grant English","content":"The Winter Soldier has all the action, quirky one-liners, and bold adventure that we have come to expect of a Marvel movie.\r\n\r\nPlus we get introduced to a new/old villain as well as some fun romantic tension between Steve Rogers (Captain America) and Natasha Romanoff (The Black Widow).  (Scarlett Johansson is perfect in this role.)\r\n\r\nBut this film also has some minor irritations all stemming from Marvel's tendency to make _Captain America_ more complicated than it really needs to be.\r\n\r\nMINOR SPOILER ALERT:\r\n1. The love story/non-story.  Is this necessary? Will the Captain remain faithful to a 80 year-old woman on her deathbed OR engage in a date with his next door neighbor?  Plus the tension between him and Natasha Romanoff is intense.  This comes off as forced, awkward, and pointless.\r\n\r\n2. \u201cThe deception goes deeper than you can imagine. You can\u2019t trust anybody.\u201d  It's this way every time so it should no longer be a surprise.\r\n\r\n3. He\u2019s dead but not really. Not him, the other him. Well\u2026they both were dead \u2013 not really \u2013 but now they are both alive. But one of them can\u2019t remember who he really is. In this film we get to do this with two characters instead of one.  It\u2019s not a huge distraction but why does Marvel keep returning to this plot line? \r\n\r\nEND OF MINOR SPOILER ALERT\r\n\r\nThe Winter Soldier is best when Rogers and Romanoff are on the move trying to put the pieces of the puzzle together. The explosions are nice, the action scenes are intense but the real enjoyment of the film is where skill and wits are relied on more than gadgets and explosions. \r\n\r\nPatriotism is a bit more nuanced this time around and it's better for it.  The cast is great, no awkward acting moments to report. It\u2019s a great summer flick with all the action you want.","url":"https://www.themoviedb.org/review/54590aff0e0a261165002061"},{"id":"55e43b63c3a368418e0035a3","author":"John Chard","content":"Till the end of the road.\r\n\r\nStonking! Now this is more like it, after the disappointments that have been Iron Man 2 and Thor 2, Captain America gets a sequel of substance and sparks. It manages to blend everything required to make a great superhero film, lashings of derring-do heroics, action bonanza, adventure, some sexy sizzle and of course the key, a story with brains and mystery elements.\r\n\r\nTrue enough to say that the considerable contributions of Black Widow and Falcon (and Nick Fury of course) keeps this as a lively Avengers spin-off movie, which is no bad thing at all, but it's still the Captain who dominates things, marking himself out as a viable main man. In fact the whole film has the old school comic book feel, yes there's the grandiose pyrotechnics - unsurprisingly amped up for the big finale, but there's an adherence to serial thriller conventions that is, well, rather warm and comforting.\r\n\r\nWith the Captain getting some surprising amount of emotional depth and humanity courtesy of the perfectly cast Chris Evans, the viewers have much to care about. Themes of Bondian world domination and global security crisis keep things nice and fanciful, while the unheralded work by the effects guys is modern cinematic art. There's some adherence to genre formula, and a bit of Marvel universe copy-catting going on, but this is one of the best films from the Marvel stable. A rich sequel indeed - more Captain America please. 9/10","url":"https://www.themoviedb.org/review/55e43b63c3a368418e0035a3"}]
     * total_pages : 1
     * total_results : 5
     */

    private int id;
    private int page;
    private int total_pages;
    private int total_results;
    /**
     * id : 535868070e0a26067f0007d2
     * author : Raquel98
     * content : This movie is very good I loved it so much.
     * url : https://www.themoviedb.org/review/535868070e0a26067f0007d2
     */

    private List<Review> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }

    public static class Review {
        private String id;
        private String author;
        private String content;
        private String url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
