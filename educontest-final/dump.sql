SELECT * FROM competition
  LEFT OUTER JOIN school_competition ON competition.competitionId = school_competition.competitionId
WHERE school_competition.`competitionId` is NULL
school_competition.schoolId = 4


-- SELECT * FROM competition
-- LEFT OUTER JOIN (school_competition JOIN school ON (school_competition.schoolId = school.schoolId));


-- SELECT acItemCustomization.*, acItems.ItemNo FROM acItems
-- LEFT OUTER JOIN acItemCustomization ON acItemCustomization.ItemID = acItems.ItemID
-- acItems.ItemNo LIKE '%PLQ144%'
-- AND acItemCustomization.ItemID IS NULL
-- AND ItemNo <> 'PLQ144';
SELECT * FROM competition
  INNER JOIN school_competition
    ON competition.competitionId = school_competition.competitionId
       AND school_competition.schoolId = '4';
