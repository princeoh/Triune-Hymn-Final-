package com.example.data.datasource

import com.example.data.model.Hymn

object SeedHymns {
    val initialHymns: List<Hymn> = listOf(
        Hymn(
            id = "1",
            number = 1,
            title = "Holy, Holy, Holy! Lord God Almighty",
            category = "Holy Trinity",
            keySignature = "E Major",
            author = "Reginald Heber (1826)",
            lyrics = """
1. Holy, holy, holy! Lord God Almighty!
Early in the morning our song shall rise to Thee;
Holy, holy, holy! Merciful and mighty!
God in three Persons, blessed Trinity!

2. Holy, holy, holy! All the saints adore Thee,
Casting down their golden crowns around the glassy sea;
Cherubim and seraphim falling down before Thee,
Which wert, and art, and evermore shalt be.

3. Holy, holy, holy! Though the darkness hide Thee,
Though the eye of sinful man Thy glory may not see;
Only Thou art holy; there is none beside Thee,
Perfect in power, in love, and purity.

4. Holy, holy, holy! Lord God Almighty!
All Thy works shall praise Thy Name, in earth, and sky, and sea;
Holy, holy, holy! Merciful and mighty!
God in three Persons, blessed Trinity!
            """.trimIndent()
        ),
        Hymn(
            id = "2",
            number = 2,
            title = "Praise to the Lord, the Almighty",
            category = "Praise & Adoration",
            keySignature = "G Major",
            author = "Joachim Neander (1680)",
            lyrics = """
1. Praise to the Lord, the Almighty, the King of creation!
O my soul, praise Him, for He is thy health and salvation!
All ye who hear, now to His temple draw near;
Praise Him in glad adoration.

2. Praise to the Lord, who o'er all things so wondrously reigneth,
Shelters thee under His wings, yea, so gently sustaineth!
Hast thou not seen how thy desires e'er have been
Granted in what He ordaineth?

3. Praise to the Lord, who doth prosper thy work and defend thee;
Surely His goodness and mercy here daily attend thee.
Ponder anew what the Almighty can do,
If with His love He befriend thee.

4. Praise to the Lord, O let all that is in me adore Him!
All that hath life and breath, come now with praises before Him.
Let the Amen sound from His people again,
Gladly for aye we adore Him.
            """.trimIndent()
        ),
        Hymn(
            id = "3",
            number = 3,
            title = "Great Is Thy Faithfulness",
            category = "Faith & Trust",
            keySignature = "D Major",
            author = "Thomas O. Chisholm (1923)",
            chorus = """
Great is Thy faithfulness!
Great is Thy faithfulness!
Morning by morning new mercies I see;
All I have needed Thy hand hath provided—
Great is Thy faithfulness, Lord, unto me!
            """.trimIndent(),
            lyrics = """
1. Great is Thy faithfulness, O God my Father,
There is no shadow of turning with Thee;
Thou changest not, Thy compassions, they fail not;
As Thou hast been Thou forever wilt be.

[Chorus]

2. Summer and winter, and springtime and harvest,
Sun, moon and stars in their courses above,
Join with all nature in manifold witness
To Thy great faithfulness, mercy and love.

[Chorus]

3. Pardon for sin and a peace that endureth,
Thine own dear presence to cheer and to guide;
Strength for today and bright hope for tomorrow,
Blessings all mine, with ten thousand beside!

[Chorus]
            """.trimIndent()
        ),
        Hymn(
            id = "4",
            number = 4,
            title = "All Hail the Power of Jesus' Name",
            category = "Praise & Adoration",
            keySignature = "F Major",
            author = "Edward Perronet (1779)",
            lyrics = """
1. All hail the power of Jesus' name!
Let angels prostrate fall;
Bring forth the royal diadem,
And crown Him Lord of all;
Bring forth the royal diadem,
And crown Him Lord of all.

2. Crown Him, ye martyrs of our God,
Who from His altar call;
Extol the Stem of Jesse's rod,
And crown Him Lord of all;
Extol the Stem of Jesse's rod,
And crown Him Lord of all.

3. Ye seed of Israel's chosen race,
Ye ransomed from the fall,
Hail Him who saves you by His grace,
And crown Him Lord of all;
Hail Him who saves you by His grace,
And crown Him Lord of all.

4. Let every kindred, every tribe,
On this terrestrial ball,
To Him all majesty ascribe,
And crown Him Lord of all;
To Him all majesty ascribe,
And crown Him Lord of all.
            """.trimIndent()
        ),
        Hymn(
            id = "5",
            number = 5,
            title = "Blessed Assurance, Jesus Is Mine",
            category = "Faith & Trust",
            keySignature = "D Major",
            author = "Fanny Crosby (1873)",
            chorus = """
This is my story, this is my song,
Praising my Savior all the day long;
This is my story, this is my song,
Praising my Savior all the day long.
            """.trimIndent(),
            lyrics = """
1. Blessed assurance, Jesus is mine!
O what a foretaste of glory divine!
Heir of salvation, purchase of God,
Born of His Spirit, washed in His blood.

[Chorus]

2. Perfect submission, perfect delight,
Visions of rapture now burst on my sight;
Angels descending, bring from above
Echoes of mercy, whispers of love.

[Chorus]

3. Perfect submission, all is at rest,
I in my Savior am happy and blest;
Watching and waiting, looking above,
Filled with His goodness, lost in His love.

[Chorus]
            """.trimIndent()
        ),
        Hymn(
            id = "6",
            number = 6,
            title = "The Church's One Foundation",
            category = "The Church & Unity",
            keySignature = "E Flat Major",
            author = "Samuel J. Stone (1866)",
            lyrics = """
1. The Church's one foundation
Is Jesus Christ her Lord;
She is His new creation
By water and the Word:
From heaven He came and sought her
To be His holy bride;
With His own blood He bought her,
And for her life He died.

2. Elect from every nation,
Yet one o'er all the earth,
Her charter of salvation
One Lord, one faith, one birth;
One holy name she blesses,
Partakes one holy food,
And to one hope she presses,
With every grace endued.

3. Though with a scornful wonder
Men see her sore oppressed,
By schisms rent asunder,
By heresies distressed,
Yet saints their watch are keeping,
Their cry goes up, "How long?"
And soon the night of weeping
Shall be the morn of song.
            """.trimIndent()
        ),
        Hymn(
            id = "7",
            number = 7,
            title = "To God Be the Glory",
            category = "Praise & Adoration",
            keySignature = "A Flat Major",
            author = "Fanny Crosby (1875)",
            chorus = """
Praise the Lord, praise the Lord,
Let the earth hear His voice!
Praise the Lord, praise the Lord,
Let the people rejoice!
O come to the Father, through Jesus the Son,
And give Him the glory, great things He hath done.
            """.trimIndent(),
            lyrics = """
1. To God be the glory, great things He hath done,
So loved He the world that He gave us His Son,
Who yielded His life an atonement for sin,
And opened the life gate that all may go in.

[Chorus]

2. O perfect redemption, the purchase of blood,
To every believer the promise of God;
The vilest offender who truly believes,
That moment from Jesus a pardon receives.

[Chorus]

3. Great things He hath taught us, great things He hath done,
And great our rejoicing through Jesus the Son;
But purer, and higher, and greater will be
Our wonder, our transport, when Jesus we see.

[Chorus]
            """.trimIndent()
        ),
        Hymn(
            id = "8",
            number = 8,
            title = "Immortal, Invisible, God Only Wise",
            category = "Holy Trinity",
            keySignature = "A Flat Major",
            author = "Walter Chalmers Smith (1867)",
            lyrics = """
1. Immortal, invisible, God only wise,
In light inaccessible hid from our eyes,
Most blessed, most glorious, the Ancient of Days,
Almighty, victorious, Thy great name we praise.

2. Unresting, unhasting, and silent as light,
Nor wanting, nor wasting, Thou rulest in might;
Thy justice, like mountains, high soaring above
Thy clouds, which are fountains of goodness and love.

3. To all, life Thou givest, to both great and small;
In all life Thou livest, the true life of all;
We blossom and flourish as leaves on the tree,
And wither and perish—but naught changeth Thee.

4. Great Father of glory, pure Father of light,
Thine angels adore Thee, all veiling their sight;
All praise we would render; O help us to see
'Tis only the splendor of light hideth Thee.
            """.trimIndent()
        ),
        Hymn(
            id = "9",
            number = 9,
            title = "What a Friend We Have in Jesus",
            category = "Prayer & Devotion",
            keySignature = "F Major",
            author = "Joseph M. Scriven (1855)",
            lyrics = """
1. What a friend we have in Jesus,
All our sins and griefs to bear!
What a privilege to carry
Everything to God in prayer!
O what peace we often forfeit,
O what needless pain we bear,
All because we do not carry
Everything to God in prayer!

2. Have we trials and temptations?
Is there trouble anywhere?
We should never be discouraged;
Take it to the Lord in prayer.
Can we find a friend so faithful
Who will all our sorrows share?
Jesus knows our every weakness;
Take it to the Lord in prayer.

3. Are we weak and heavy laden,
Cumbered with a load of care?
Precious Savior, still our refuge;
Take it to the Lord in prayer.
Do thy friends despise, forsake thee?
Take it to the Lord in prayer!
In His arms He'll take and shield thee;
Thou wilt find a solace there.
            """.trimIndent()
        ),
        Hymn(
            id = "10",
            number = 10,
            title = "Crown Him with Many Crowns",
            category = "Praise & Adoration",
            keySignature = "D Major",
            author = "Matthew Bridges (1851)",
            lyrics = """
1. Crown Him with many crowns,
The Lamb upon His throne;
Hark! how the heavenly anthem drowns
All music but its own:
Awake, my soul, and sing
Of Him who died for thee,
And hail Him as thy matchless King
Through all eternity.

2. Crown Him the Lord of love;
Behold His hands and side,
Rich wounds, yet visible above,
In beauty glorified:
No angel in the sky
Can fully bear that sight,
But downward bends his burning eye
At mysteries so bright.

3. Crown Him the Lord of peace,
Whose power a scepter sways
From pole to pole, that wars may cease,
And all be prayer and praise:
His reign shall know no end,
And round His pierced feet
Fair flowers of paradise extend
Their fragrance ever sweet.
            """.trimIndent()
        )
    )
}
