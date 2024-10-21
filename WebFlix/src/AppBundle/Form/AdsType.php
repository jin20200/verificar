<?php
namespace AppBundle\Form;

use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\FormEvent;
use Symfony\Component\Form\FormEvents;
use Ivory\CKEditorBundle\Form\Type\CKEditorType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;

class AdsType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options)
    {

        $builder->add('bannerfacebookid',ChoiceType::class, array(
                "label"=>"Rewarded Ad Type",
                'choices' => array(
                    "Disable Ad Rewarded" => "FALSE",
                    "Google AdMob Ad Rewarded" => "ADMOB",
                    "AppLovin MAX Rewarded" => "MAX",
                    "ironSource Rewarded" => "IS"
                )));
        $builder->add('rewardedadmobid',null,array("label"=>"Rewarded Ad ID "));
        $builder->add('banneradmobid',null,array("label"=>"Banner Ad  ID "));
        $builder->add('bannertype',ChoiceType::class, array(
                "label"=>"Banner Ad Type",
                'choices' => array(
                    "Disable Ad Banner" => "FALSE",
                    "Google AdMob Ad Banner" => "ADMOB",
                    "AppLovin MAX Banner" => "MAX",
                    "ironSource Banner" => "IS"

                )));
        $builder->add('nativeadmobid',null,array("label"=>"AdMob Native ID "));
        $builder->add('nativetype',ChoiceType::class, array(
                "label"=>"Native Ad Type",
                'choices' => array(
                    "Disable Ad Native" => "FALSE",
                    "Google AdMob Ad Native"=> "ADMOB",
                     "AppLovin MAX Native" => "MAX"
                )));
        $builder->add('nativeitem',null,array("label"=>"Native Ad Type "));
        $builder->add('interstitialadmobid',null,array("label"=>"AdMob Interstitial ID "));
        $builder->add('interstitialtype',ChoiceType::class, array(
                "label"=>"Interstitial Ad Type",
                'choices' => array(
                    "Disable Ad Interstitial"=> "FALSE",
                     "Google AdMob Ad Interstitial"=> "ADMOB",
                     "AppLovin MAX Interstitial" => "MAX",
                     "ironSourceInterstitial" => "IS"

                )));    
        $builder->add('interstitialclick',null,array("label"=>"Interstitial Ad Type "));

        $builder->add('save', SubmitType::class,array("label"=>"SAVE"));
    }
    public function getName()
    {
        return 'Ads';
    }
}
?>